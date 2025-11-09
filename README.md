# CryptoCreep Telegram Bot

CryptoCreep is a Java-based Telegram bot that fetches spot prices from the Binance public API and delivers them to Telegram users on demand. Users can retrieve the latest price for a supported trading pair once or subscribe to periodic updates using Telegram's custom reply keyboards.

## Features

- Interactive Telegram bot powered by the [`telegrambots`](https://github.com/rubenlagus/TelegramBots) Java library
- Predefined keyboards that guide users through common actions (choose an operation, pick a trading pair, and select an update frequency)
- One-off price checks or recurring notifications for selected pairs
- Resilient Binance API connector with lightweight retry handling and rate-limit friendly delays
- Dockerfile for containerized deployments

## Project Structure

```
├── src/main/java/dev/march
│   ├── Main.java                # Bootstraps the Telegram long-polling bot session
│   ├── Bot.java                 # Conversation flow, keyboards, and scheduling logic
│   ├── BinanceConnector.java    # HTTP client for Binance REST endpoints
│   ├── BinanceDataParser.java   # JSON parsing helpers for ticker responses
│   ├── Ticker.java              # Plain object representing a Binance ticker entry
│   └── keyboards/               # Reply keyboard builders used by the bot
├── pom.xml                      # Maven build configuration
└── Dockerfile                   # Multi-stage build producing a runnable JAR image
```

## Prerequisites

- Java 17 or later
- Maven 3.9+
- A Telegram bot token and bot username obtained from [@BotFather](https://t.me/BotFather)
- Internet access to reach the Binance REST API (`https://api.binance.com`)

## Configuration

The bot reads its configuration from environment variables:

| Variable   | Description                                        |
|------------|----------------------------------------------------|
| `BOT_NAME` | Public username of your Telegram bot (without `@`)
| `BOT_TOKEN`| API token provided by BotFather                     |

Set these variables in your shell before launching the application, for example:

```bash
export BOT_NAME="CryptoCreepBot"
export BOT_TOKEN="1234567890:abcdefghijklmnopqrstuvwxyz"
```

> **Note:** If you are running the bot inside Docker, pass these variables via `-e` flags or your orchestrator's secret management system.

## Running Locally

1. Install the prerequisites and set the environment variables described above.
2. Build the shaded JAR that bundles all dependencies:

   ```bash
   mvn clean package -DskipTests
   ```

3. Start the bot:

   ```bash
   java -jar target/CryptoCreepBot-1.0-SNAPSHOT.jar
   ```

4. Open Telegram, start a conversation with your bot, and send `/start` to begin.

## Docker Usage

A multi-stage `Dockerfile` is included for convenience.

```bash
docker build -t cryptocreep-bot .
docker run -e BOT_NAME="CryptoCreepBot" -e BOT_TOKEN="<your-token>" cryptocreep-bot
```

The resulting container runs the shaded JAR produced by the Maven build stage.
