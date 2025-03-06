# TgWebhookBotCore

TgWebhookBotCore is a Spring Boot project that enables you to manage multiple Telegram bots in real time using webhooks. The application provides RESTful API endpoints for creating, updating, retrieving, and deleting bot configurations, as well as registering and unregistering bots on demand.

## Features

- **Multiple Bot Management:** Easily register and manage multiple Telegram bots in a single project.
- **Real-Time Webhook Integration:** Uses webhooks to handle real-time updates from Telegram.
- **Lifecycle Event Handling:** Automatically registers all bots on startup and unregisters them on shutdown.
- **Comprehensive REST API:** Endpoints to create, update, get, delete, register, unregister, and check the health of bots.

## Technologies

- **Spring Boot 2.7.18**
- **Springdoc OpenAPI UI (v1.6.14):** For API documentation.
- **Telegram Bots Spring Boot Starter (v6.9.0):** For interacting with the Telegram API.
- **Webhook Method:** For receiving bot updates in real time.

## Getting Started

### Prerequisites

- **Java 11** (or higher)
- **Maven 3.6** (or higher)

### Installation

1. **Clone the repository:**

   ```bash
   git clone https://github.com/IxtiyorXaitov/TgWebhookBotCore.git
   cd TgWebhookBotCore
