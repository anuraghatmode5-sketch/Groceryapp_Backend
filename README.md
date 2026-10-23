# GroceryApp — Backend

A REST API backend for a full-stack grocery e-commerce application built with Java and Spring Boot. The application supports user and seller authentication, product management, shopping cart operations, address management, order processing, and online payments.

## 🚀 Features

- **User Authentication:** User registration, login, logout, and authentication status checks.
- **Seller Authentication:** Separate seller login and authentication workflows.
- **JWT Authentication:** Token-based authentication with distinct USER and SELLER roles.
- **Product Management:** Retrieve product listings, fetch product details, add products, and manage stock.
- **Shopping Cart:** Update cart items and remove products from the cart.
- **Address Management:** Add and retrieve user delivery addresses.
- **Order Management:** Place Cash on Delivery (COD) orders, create Razorpay orders, and retrieve order information.
- **Payment Verification:** Verify Razorpay payment signatures and save orders after successful verification.
- **Image Uploads:** Integrate Cloudinary for product image storage.
- **Database Integration:** Store application data using MongoDB.

## 🛠️ Tech Stack

- **Language:** Java
- **Framework:** Spring Boot
- **Security:** Spring Security, JWT
- **Database:** MongoDB
- **Payment Gateway:** Razorpay
- **Image Storage:** Cloudinary
- **Build Tool:** Maven
- **API Style:** REST APIs

## 🏗️ Architecture

The backend uses a layered architecture to separate responsibilities:

- **Controller Layer:** Handles HTTP requests and responses.
- **Service Layer:** Implements business logic.
- **Repository Layer:** Handles database operations.
- **Model / DTO:** Represents application data and API request/response objects.

## 🔌 API Overview

The following endpoints are relative to the `/api` base path.

| Feature | Endpoint | Method |
|---|---|---|
| Add address | `/address/add` | POST |
| Retrieve addresses | `/address/get` | GET |
| Update cart | `/cart/update` | POST |
| Remove cart item | `/cart/remove` | DELETE |
| Place COD order | `/order/cod` | POST |
| Create Razorpay order | `/order/razorpay` | POST |
| Retrieve all orders | `/order/all` | GET |
| Retrieve user orders | `/order/user/{userId}` | GET |
| Verify payment | `/order/verify-payment` | POST |
| List products | `/product/list` | GET |
| Retrieve product details | `/product/id` | GET |
| Add product | `/product/add` | POST |
| Update product | `/product/id` | POST |
| Update stock | `/product/stock` | POST |
| Seller login | `/seller/login` | POST |
| Check seller authentication | `/seller/is-auth` | GET |
| Seller logout | `/seller/logout` | POST |
| User login | `/user/login` | POST |
| User logout | `/user/logout` | POST |
| Check user authentication | `/user/is-auth` | GET |
| Register user | `/users/register` | POST |

## ⚙️ Getting Started

### Prerequisites

- Java Development Kit (JDK) compatible with the project
- Git
- Maven, or the included Maven Wrapper
- MongoDB database
- Razorpay account credentials for payment functionality
- Cloudinary account credentials for image uploads

### 1. Clone the repository

```bash
git clone https://github.com/anuraghatmode5-sketch/Groceryapp_Backend.git
cd Groceryapp_Backend
```

### 2. Configure environment properties

Configure the application's required properties for:

- MongoDB connection
- JWT configuration
- Razorpay API credentials
- Cloudinary credentials
- Any other application-specific settings

Use your local configuration or environment variables as appropriate for the project.

**Important:** Never commit API secrets, database passwords, or private credentials to GitHub.

### 3. Run the application

On Windows:

```bash
mvnw.cmd spring-boot:run
```

On macOS/Linux:

```bash
./mvnw spring-boot:run
```

The application will start on the port configured in the application properties.

## 💳 Razorpay Payment Flow

1. The client requests creation of a Razorpay order.
2. The backend creates the payment order through Razorpay.
3. The client completes the payment using the payment gateway.
4. The client sends the payment details and signature to the verification endpoint.
5. The backend verifies the payment signature through its Razorpay service.
6. The order is saved as paid only after successful verification.

## 🔐 Authentication

The application separates user and seller authentication and uses JWT-based authentication with USER and SELLER roles. Protected operations rely on the configured Spring Security authentication mechanism.

## 🧪 API Testing

You can use Postman to test the backend workflows:

1. Register a user and test authentication.
2. Log in as a user and as a seller.
3. Retrieve products and update the cart.
4. Add and retrieve delivery addresses.
5. Place a COD order.
6. Create and verify a Razorpay payment.
7. Retrieve order information.

## 👨‍💻 Author

**Anurag Gajanan Hatmode**

GitHub: [@anuraghatmode5-sketch](https://github.com/anuraghatmode5-sketch)

---

Built as a full-stack e-commerce project to practise REST API development, authentication, database integration, and third-party payment and image-storage services.
