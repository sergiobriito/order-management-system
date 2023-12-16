const config = {
  API_URL: 'http://localhost:8080/api/v1/order-system',
  SOCKET_URL: 'http://localhost:8080/ws',
};

let stompClient = null;

document.addEventListener('DOMContentLoaded', () => {
  getOrdersInProgress();
  connectWithSocket();
  loadMessagesFromLocalStorage();
});

async function getOrdersInProgress() {
  try {
    const ordersInProgress = await fetchDataFromAPI(`${config.API_URL}/orders-in-progress`);

    if (ordersInProgress) {
      const ordersList = document.getElementById('inProgressList');
      ordersList.innerHTML = '';

      ordersInProgress.forEach(order => {
        const listItem = createOrderListItem(order);
        ordersList.appendChild(listItem);
      });
    }
  } catch (error) {
    console.error('Error getting orders:', error);
  }
}

function createOrderListItem(order) {
  const listItem = document.createElement('li');
  listItem.id = order.id;
  listItem.className = 'order-item';

  let ownerText = order.owner;
  let productText = order.product;
  let descriptionText = order.description !== null ? `(${order.description})` : '';
  listItem.textContent = `${ownerText} -> ${productText} ${descriptionText}`;
  
  const completeBtn = document.createElement('button');
  completeBtn.className = 'complete-btn';
  completeBtn.textContent = 'Complete Order';
  completeBtn.addEventListener('click', () => {
    completeOrder(order.id);
  });

  listItem.appendChild(completeBtn);
  return listItem;
}

async function completeOrder(orderId) {
  try {
    await patchData(`${config.API_URL}/complete-order/${orderId}`).then(()=>{location.reload();});
  } catch (error) {
    console.error('Error completing order:', error);
  }
}

document.getElementById('postForm').addEventListener('submit', async function (event) {
  event.preventDefault();

  const formData = new FormData(this);
  const requestData = {};

  for (const [key, value] of formData.entries()) {
    requestData[key] = value;
  }

  try {
    const response = await fetch(`${config.API_URL}/save-order`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(requestData),
    });

    if (!response.ok) {
      throw new Error('Network response was not ok.');
    }

    const data = await response.json();

    location.reload();
  } catch (error) {
    console.error('Error:', error);
  }
});

async function fetchDataFromAPI(apiUrl) {
  try {
    const response = await fetch(apiUrl);
    if (!response.ok) {
      throw new Error('Request failed!');
    }
    return await response.json();
  } catch (error) {
    console.error('Error fetching data:', error);
    return null;
  }
}

async function patchData(url = '', data = {}) {
  try {
    const response = await fetch(url, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data)
    });

    if (!response.ok) {
      throw new Error('Network response was not ok.');
    }
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
}

function connectWithSocket() {
  var socket = new SockJS(config.SOCKET_URL);
  stompClient = Stomp.over(socket);
  stompClient.connect({}, function (frame) {
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/messages', function (message) {
      showMessages(JSON.parse(message.body));
    });
  });
}

function showMessages(message) {
  saveMessageToLocalStorage(message);
}

function saveMessageToLocalStorage(message) {
  let messages = JSON.parse(localStorage.getItem('storedMessages'));
  if (!Array.isArray(messages)) {
    messages = [];
  }
  messages.push(message);
  localStorage.setItem('storedMessages', JSON.stringify(messages));
}

function loadMessagesFromLocalStorage() {
  const storedMessages = JSON.parse(localStorage.getItem('storedMessages'));
  if (Array.isArray(storedMessages) && storedMessages.length > 0) {
    storedMessages.forEach(message => {
      $("#ordersList").append("<tr><td class='order-card' style='text-align: center;'>" + message.Owner + " -> " + message.Product + "</td></tr>");
    });
  }
}

document.getElementById('clearLocalStorageBtn').addEventListener('click', () => {
  clearLocalStorage();
});

function clearLocalStorage() {
  localStorage.removeItem('storedMessages');
  $("#ordersList").empty();
}