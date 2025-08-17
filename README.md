# 🌐 TranslateX - Advanced Translation Plugin

**TranslateX** is an advanced Minecraft plugin that enables real-time translation of chat messages and items with OpenAI API support.

## ✨ Features

### 🎯 **Hover Functionality**
- **Always see original language**: You always see the message in the player's original language
- **Hover for translation**: Move your mouse over the message to see the translation
- **Beautiful hover interface**: Shows original text, source and target language with flags

### 🌍 **AI-Powered Translation**
- **OpenAI API**: High-quality AI-powered translations
- **Smart language detection**: Automatically detects source language
- **Multiple language support**: 30+ supported languages

### 🎮 **User-Friendly Interface**
- **Beautiful GUI menu**: Select your language with a visual menu
- **Language flags**: Recognize languages quickly with flags
- **Native names**: See language names in their own script

### ⚡ **Performance Features**
- **Caching system**: Store translations for fast access
- **Async processing**: Non-blocking translations
- **Smart optimization**: Efficient API usage

### 🔧 **Server Compatibility**
- **Paper**: Full support for 1.13+ servers  
- **Spigot**: Full support for 1.8.8+ servers
- **Automatic detection**: Plugin automatically detects your server type

## 🚀 Installation

### Requirements
- **Minecraft**: 1.8.8 - 1.21.6 (Spigot/Paper)
- **Java**: 8 or higher
- **API Key**: OpenAI API key

### Supported Server Types
- **Paper**: 1.13+ (Recommended for most servers)
- **Spigot**: 1.8.8+ (Legacy support)

### Steps
1. Download the latest version of TranslateX
2. Place the JAR in your `plugins` folder
3. Start your server
4. Configure your OpenAI API key in `config.yml`
5. Restart your server

## ⚙️ Configuration

### Setting OpenAI API Key
```yaml
TRANSLATION_PROVIDERS:
  openai:
    enabled: true
    api_keys:
      - "YOUR_OPENAI_API_KEY"
    model: "gpt-3.5-turbo"
    max_tokens: 150
    temperature: 0.3
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
  caching:
    max_cache_size: 10000
    expiration_time: 60
  
  async:
    thread_pool_size: 4
```

## 🐛 Troubleshooting

### Common Problems

**Plugin won't start:**
- Check if you're using Java 8+
- Check your OpenAI API key in config.yml
- Look at the console for error messages

**Translations not working:**
- Check if your OpenAI API key is valid
- Check your internet connection
- See if OpenAI API is available

**Performance issues:**
- Reduce the cache size
- Check your OpenAI API quotas

### Server Compatibility Issues

**Paper servers:**
- Ensure you're running 1.13 or higher
- Some features may not work on older versions

**Spigot servers:**
- Ensure you're running 1.8.8 or higher
- Hover functionality may be limited on older versions

## 🔄 Updates

### From Older Versions
- Backup your config.yml
- Update to the new version
- Restart your server
- Check the new configuration options

## 📝 Changelog

### v1.0.0
- ✨ New hover functionality
- 🌍 OpenAI API integration
- 🎮 Beautiful GUI language selection menu
- ⚡ Performance improvements
- 🔧 Extensive configuration options
- 📱 Modern Minecraft version support
- 🚀 **NEW**: OpenAI-powered translations
- 🔧 **NEW**: Enhanced server compatibility
- 📊 **NEW**: Automatic language detection

## 🤝 Support

- **Discord**: [https://discord.rubixdevelopment.nl/](https://discord.rubixdevelopment.nl/)
- **GitHub**: [https://github.com/Rubix-Development/TranslateX](https://github.com/Rubix-Development/TranslateX)
- **Issues**: Report bugs via GitHub Issues

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Credits

- **Development**: Djorr (Rubix Development)
- **API**: OpenAI
- **Community**: All users and contributors

---

**🌐 Make your server accessible to players from around the world with TranslateX!**

**🔧 Now supporting Paper and Spigot servers with OpenAI!**
