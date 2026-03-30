# 🤖 AI Phone App

<p align="center">
  <img src="https://img.shields.io/github/v/release/bitplugg/ai-phone-app?include_prereleases&style=flat&logo=github" alt="GitHub release">
  <img src="https://img.shields.io/github/last-commit/bitplugg/ai-phone-app" alt="last commit">
  <img src="https://img.shields.io/github/downloads/bitplugg/ai-phone-app/total" alt="downloads">
  <img src="https://img.shields.io/github/license/bitplugg/ai-phone-app" alt="license">
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Android-7.0%2B-brightgreen?style=flat&logo=android" alt="android">
  <img src="https://img.shields.io/badge/Kotlin-2.0-blue?style=flat&logo=kotlin" alt="kotlin">
  <img src="https://img.shields.io/badge/Jetpack%20Compose-2024.02-orange?style=flat&logo=jetpackcompose" alt="compose">
  <img src="https://img.shields.io/badge/Size-~15MB-blueviolet?style=flat" alt="size">
</p>

> AI Assistant для Android с поддержкой Ollama, OpenAI и DeepSeek. 100% бесплатно и с открытым исходным кодом!

## ✨ Возможности

### 🔌 AI Режимы
| Возможность | Описание |
|------------|----------|
| **Ollama** | Подключение к локальному Ollama серверу через WiFi |
| **OpenAI** | Использование GPT-4, GPT-3.5 Turbo |
| **DeepSeek** | Дешевые и мощные модели DeepSeek |

### 🎨 Интерфейс
| Возможность | Описание |
|------------|----------|
| **Material You** | Динамические цвета из темы системы (Android 12+) |
| **6 Тем** | Purple, Blue, Green, Orange, Red, System |
| **85+ Настроек** | Полная кастомизация приложения |

### 💬 Чат
| Возможность | Описание |
|------------|----------|
| **Голосовой ввод** | Speech-to-Text через Android |
| **TTS** | Озвучивание ответов AI |
| **Реакции** | Emoji на сообщениях 👍❤️😂 |
| **Поиск** | Поиск по чатам и сообщениям |
| **Категории** | Группировка чатов |
| **Pin/Mute** | Закрепление и отключение уведомлений |
| **Markdown** | Красивое отображение кода |

### 💾 Данные
| Возможность | Описание |
|------------|----------|
| **Экспорт** | TXT и JSON |
| **Backup** | Резервное копирование настроек |
| **История** | Множество чатов |

### 🔔 Уведомления
| Возможность | Описание |
|------------|----------|
| **Telegram Bot** | Пересылка ответов в Telegram |
| **Настройки** | Звук, вибрация, время |

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

APK появится в `app/build/outputs/apk/debug/`

## ⚙️ Настройка AI

### Ollama (Дома, бесплатно)
1. Установи [Ollama](https://github.com/ollama/ollama) на ПК
2. Запусти: `ollama serve`
3. Скачай модель: `ollama pull llama3`
4. Узнай IP ПК: `ipconfig` (Windows) / `ip a` (Linux)
5. В приложении: Настройки → WiFi → IP (например `http://192.168.1.100:11434`)

### OpenAI
1. Получи API ключ на [platform.openai.com](https://platform.openai.com/api-keys)
2. В приложении: Настройки → Облако → OpenAI → Введи ключ

### DeepSeek (дешево)
1. Получи API ключ на [platform.deepseek.com](https://platform.deepseek.com)
2. В приложении: Настройки → Облако → DeepSeek → Введи ключ

## 🛠️ Технологии

- **Kotlin** 2.0
- **Jetpack Compose** (Material 3 + Material You)
- **MVVM** Architecture
- **Coroutines + Flow**
- **OkHttp** для API запросов
- **DataStore** для настроек
- **GitHub Actions** для CI/CD

## 📁 Структура проекта

```
ai-phone-app/
├── app/src/main/java/com/aiapp/
│   ├── MainActivity.kt           # Главная активность
│   ├── data/
│   │   ├── ai/                    # AI сервисы
│   │   │   ├── AIService.kt       #Dual AI режим
│   │   │   └── OllamaService.kt   # Ollama API
│   │   └── local/
│   │       ├── InMemoryStorage.kt # Хранилище чатов
│   │       └── PreferencesManager.kt # 85+ настроек
│   ├── viewmodel/
│   │   └── AppViewModel.kt        # ViewModel
│   └── ui/
│       ├── screens/               # Экраны
│       │   ├── MainScreen.kt
│       │   ├── SettingsScreen.kt
│       │   └── SearchScreen.kt
│       └── theme/
│           └── Theme.kt           # Material You тема
├── .github/
│   └── workflows/                # 12 GitHub Actions
│       ├── lint.yml
│       ├── test.yml
│       ├── security.yml
│       ├── build.yml
│       ├── release.yml
│       ├── pages.yml
│       ├── ci-dashboard.yml
│       └── ...
├── docs/
│   └── index.html                 # Сайт
├── build.sh                       # TUI скрипт сборки
└── testbug.sh                     # TUI скрипт тестирования
```

## 🤖 GitHub Actions (12 workflow)

| Workflow | Описание |
|---------|----------|
| `lint.yml` | Android Lint, ktlint, Detekt |
| `test.yml` | Unit тесты, JaCoCo покрытие |
| `security.yml` | Dependency review, CodeQL |
| `build.yml` | Сборка Debug/Release APK |
| `release.yml` | Авто-релизы при пуше тегов |
| `pages.yml` | GitHub Pages для сайта |
| `ci-dashboard.yml` | Сводка всех workflow |
| `instrumented-tests.yml` | Тесты на эмуляторе |
| `firebase-distribute.yml` | Firebase App Distribution |
| `docs-generator.yml` | API документация (Dokka) |
| `build-stats.yml` | Статистика сборки |
| `community.yml` | Авто-лабелинг, good first issues |

## 🤝 Contributing

1. Fork репозиторий
2. Создай branch: `git checkout -b feature/awesome`
3. Commit изменения: `git commit -m 'Add awesome feature'`
4. Push: `git push origin feature/awesome`
5. Создай Pull Request

## 📊 Статистика

- **85+** настроек приложения
- **12** GitHub Actions workflow
- **~15MB** размер APK
- **100%** бесплатно и open source

## 📄 License

MIT License - смотри [LICENSE](LICENSE)

---

<p align="center">
  Сделано с ❤️ <a href="https://github.com/bitplugg">bitplugg</a>
</p>