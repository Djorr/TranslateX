# 🌐 TranslateX - Advanced Translation Plugin

**TranslateX** is an advanced Minecraft plugin that enables real-time translation of chat messages and items with support for multiple translation APIs.

## ✨ Features

### 🎯 **Hover Functionality**
- **Always see original language**: You always see the message in the player's original language
- **Hover for translation**: Move your mouse over the message to see the translation
- **Beautiful hover interface**: Shows original text, source and target language with flags

### 🌍 **Multi-API Support**
- **DeepL API**: Highest quality translations
- **OpenAI API**: AI-powered translations
- **Google Translate**: Widely supported languages
- **Fallback system**: Automatic fallback on API errors

### 🎮 **User-Friendly Interface**
- **Beautiful GUI menu**: Select your language with a visual menu
- **Language flags**: Recognize languages quickly with flags
- **Native names**: See language names in their own script

### ⚡ **Performance Features**
- **Caching system**: Store translations for fast access
- **Async processing**: Non-blocking translations
- **Rate limiting**: Protect against API abuse
- **Smart fallback**: Automatically switch between providers

## 🚀 Installation

### Requirements
- **Minecraft**: 1.7 - 1.21.6 (Spigot/Paper)
- **Java**: 8 or higher
- **API Keys**: DeepL, OpenAI, or Google Translate

### Steps
1. Download the latest version of TranslateX
2. Place the JAR in your `plugins` folder
3. Start your server
4. Configure your API keys in `config.yml`
5. Restart your server

## ⚙️ Configuration

### Setting API Keys
```yaml
TRANSLATION_PROVIDERS:
  deepl:
    enabled: true
    api_keys:
      - "YOUR_DEEPL_API_KEY"
  
  openai:
    enabled: true
    api_keys:
      - "YOUR_OPENAI_API_KEY"
  
  google:
    enabled: true
    api_keys:
      - "YOUR_GOOGLE_API_KEY"
```

### Adjusting Features
```yaml
FEATURES:
  chat_translation:
    enabled: true
    auto_translate_incoming: true
    show_original: true
  
  item_translation:
    enabled: true
    translate_names: true
    translate_lore: true
```

## 📖 Usage

### Basic Commands
- `/translator` - Open language selection menu
- `/translator set <language>` - Set your language
- `/translator menu` - Open language menu
- `/translator info` - Show plugin information
- `/translator help` - Show help

### Setting Language
1. Use `/translator` to open the menu
2. Click on the desired language
3. Your language is automatically set
4. All messages are now translated to your language

### Chat Translation
- **Automatic**: Messages are automatically translated
- **Hover**: Move your mouse over messages to see details
- **Original**: You always see the original text

## 🌍 Supported Languages

### Popular Languages
- 🇺🇸 **English** (EN)
- 🇩🇪 **Deutsch** (DE)
- 🇫🇷 **Français** (FR)
- 🇪🇸 **Español** (ES)
- 🇮🇹 **Italiano** (IT)
- 🇳🇱 **Nederlands** (NL)
- 🇵🇱 **Polski** (PL)

### Asian Languages
- 🇯🇵 **日本語** (JA)
- 🇨🇳 **中文** (ZH)
- 🇰🇷 **한국어** (KO)
- 🇸🇦 **العربية** (AR)
- 🇮🇳 **हिन्दी** (HI)

### European Languages
- 🇷🇺 **Русский** (RU)
- 🇵🇹 **Português** (PT)
- 🇹🇷 **Türkçe** (TR)
- 🇸🇪 **Svenska** (SV)
- 🇩🇰 **Dansk** (DA)
- 🇳🇴 **Norsk** (NO)
- 🇫🇮 **Suomi** (FI)

*And many more...*

## 🔧 Permissions

```yaml
translatorx.translate          # Basic translation functionality
translatorx.translate.items    # Item translation
translatorx.admin             # Admin commands
translatorx.bypass.ratelimit  # Bypass rate limiting
```

## 📊 Performance Settings

```yaml
PERFORMANCE:
  rate_limiting:
    max_requests_per_minute: 30
    max_global_requests_per_minute: 1000
  
  caching:
    max_cache_size: 10000
    expiration_time: 60
  
  async:
    thread_pool_size: 4
```

## 🎨 Customization

### Chat Format
```yaml
USER_INTERFACE:
  chat_format:
    format: "&6&l[&e{source_lang}&6&l → &a{target_lang}&6&l] &f{message}"
    show_provider: true
```

### Hover Text
```yaml
USER_INTERFACE:
  translation_feedback:
    show_quality: true
    show_source_language: true
```

## 🐛 Troubleshooting

### Common Problems

**Plugin won't start:**
- Check if you're using Java 8+
- Check your API keys in config.yml
- Look at the console for error messages

**Translations not working:**
- Check if your API keys are valid
- Check your internet connection
- See if the API provider is available

**Performance issues:**
- Lower the rate limiting settings
- Reduce the cache size
- Check your API quotas

## 🔄 Updates

### From Older Versions
- Backup your config.yml
- Update to the new version
- Restart your server
- Check the new configuration options

## 📝 Changelog

### v1.0.0
- ✨ New hover functionality
- 🌍 Multi-API support (DeepL, OpenAI, Google)
- 🎮 Beautiful GUI language selection menu
- ⚡ Performance improvements
- 🔧 Extensive configuration options
- 📱 Modern Minecraft version support

## 🤝 Support

- **Discord**: [https://discord.rubixdevelopment.nl/](https://discord.rubixdevelopment.nl/)
- **GitHub**: [https://github.com/Rubix-Development/TranslateX](https://github.com/Rubix-Development/TranslateX)
- **Issues**: Report bugs via GitHub Issues

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Credits

- **Development**: Djorr (Rubix Development)
- **APIs**: DeepL, OpenAI, Google Translate
- **Community**: All users and contributors

---

**🌐 Make your server accessible to players from around the world with TranslateX!**
