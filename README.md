# 🤖 AI Phone App

<p align="center">
  <img src="https://img.shields.io/github/v/release/bitplugg/ai-phone-app?include_prereleases&style=flat&logo=github" alt="GitHub release">
  <img src="https://img.shields.io/github/last-commit/bitplugg/ai-phone-app" alt="last commit">
  <img src="https://img.shields.io/github/downloads/bitplugg/ai-phone-app/total" alt="downloads">
  <img src="https://img.shields.io/github/license/bitplugg/ai-phone-app" alt="license">
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Android-12%2B-brightgreen?style=flat&logo=android" alt="android">
  <img src="https://img.shields.io/badge/Kotlin-2.0-blue?style=flat&logo=kotlin" alt="kotlin">
  <img src="https://img.shields.io/badge/Jetpack%20Compose-2024.02-orange?style=flat&logo=jetpackcompose" alt="compose">
</p>

> AI Assistant для Android с поддержкой Ollama, OpenAI и DeepSeek

## ✨ Возможности

| Возможность | Описание |
|------------|----------|
| 🔌 **Ollama** | Подключение к локальному Ollama серверу через WiFi |
| ☁️ **OpenAI** | Использование GPT-4, GPT-3.5 Turbo |
| 🔥 **DeepSeek** | Дешевые и мощные модели DeepSeek |
| 🎨 **Material You** | Динамические цвета из темы системы |
| 🗣️ **Voice Input** | Голосовой ввод через Speech-to-Text |
| 🔊 **TTS** | Озвучивание ответов AI |
| 📱 **Markdown** | Красивое отображение кода и форматирования |
| 🌙 **Темы** | Светлая/тёмная тема |
| 📏 **Размер текста** | Настройка размера текста |
| 💾 **Экспорт** | Экспорт чатов в TXT |

## 📸 Скриншоты

<p align="center">
  <img src="https://via.placeholder.com/300x600/6750A4/FFFFFF?text=AI+Assistant" alt="screenshot" width="200"/>
  <img src="https://via.placeholder.com/300x600/6750A4/FFFFFF?text=Chat" alt="screenshot" width="200"/>
  <img src="https://via.placeholder.com/300x600/6750A4/FFFFFF?text=Settings" alt="screenshot" width="200"/>
</p>

## 🚀 Установка

### Вариант 1: Скачать APK
Перейди в [Releases](https://github.com/bitplugg/ai-phone-app/releases) и скачай `app-debug.apk`

### Вариант 2: Собрать самому
```bash
git clone https://github.com/bitplugg/ai-phone-app.git
cd ai-phone-app
./gradlew assembleDebug
```

## ⚙️ Настройка

### Ollama (Дома)
1. Установи [Ollama](https://github.com/ollama/ollama) на ПК
2. Запусти: `ollama serve`
3. В приложении: Настройки → WiFi → IP сервера (например `192.168.1.100:11434`)

### OpenAI
1. Получи API ключ на [platform.openai.com](https://platform.openai.com/api-keys)
2. В приложении: Настройки → Облако → Введи ключ

### DeepSeek
1. Получи API ключ на [platform.deepseek.com](https://platform.deepseek.com)
2. В приложении: Настройки → DeepSeek → Введи ключ

## 🛠️ Технологии

- **Kotlin** 2.0
- **Jetpack Compose** (Material 3)
- **MVVM** Architecture
- **Coroutines + Flow**
- **OkHttp** для网络请求
- **DataStore** для настроек

## 📁 Структура проекта

```
ai-phone-app/
├── app/src/main/
│   ├── java/com/aiapp/
│   │   ├── data/           # Data layer
│   │   │   ├── ai/          # AI services
│   │   │   └── local/       # Storage
│   │   ├── ui/              # UI layer
│   │   │   ├── screens/      # Screens
│   │   │   ├── components/  # Components
│   │   │   └── theme/       # Theme
│   │   └── viewmodel/       # ViewModels
│   └── res/                 # Resources
└── build.sh                 # Build script
```

## 🤝 Contributing

1. Fork репозиторий
2. Создай branch: `git checkout -b feature/awesome`
3. Commit изменения: `git commit -m 'Add awesome feature'`
4. Push: `git push origin feature/awesome`
5. Создай Pull Request

## 📄 License

MIT License - смотри [LICENSE](LICENSE)

---

<p align="center">
  Made with ❤️ by <a href="https://github.com/bitplugg">bitplugg</a>
</p>
