#!/bin/bash

cd "$(dirname "$0")"

export JAVA_HOME=~/jdk/jdk-21.0.2
export PATH=$JAVA_HOME/bin:$PATH

RED=$'\033[0;31m'
GREEN=$'\033[0;32m'
YELLOW=$'\033[1;33m'
CYAN=$'\033[0;36m'
MAGENTA=$'\033[0;35m'
WHITE=$'\033[1;37m'
GRAY=$'\033[0;90m'
NC=$'\033[0m'
BOLD=$'\033[1m'

submenu() {
    local title="$1"
    local options="$2"
    local handler="$3"
    
    clear
    echo ""
    echo -e "${MAGENTA}┌${WHITE}── $title ───────────────────────────────┐${NC}"
    echo -e "${MAGENTA}│${NC}  ${CYAN}Выберите:${NC}                                  ${MAGENTA}│${NC}"
    
    eval "$options"
    
    echo -e "${MAGENTA}│${NC}  ${GRAY}0) Назад${NC}                                     ${MAGENTA}│${NC}"
    echo -e "${MAGENTA}└────────────────────────────────────────────┘${NC}"
    echo ""
    echo -en "${CYAN}>${NC} "
    read choice
    
    if [ "$choice" = "0" ]; then
        return 1
    fi
    
    eval "$handler $choice"
}

menu_build() {
    while true; do
        clear
        echo ""
        echo -e "${MAGENTA}┌${WHITE}── Build ────────────────────────────────┐${NC}"
        echo -e "  ${WHITE}1)${NC} 📦 Debug APK"
        echo -e "  ${WHITE}2)${NC} 🚀 Release APK"
        echo -e "  ${WHITE}3)${NC} 🧹 Clean"
        echo -e "  ${WHITE}4)${NC} 📱 Lint"
        echo -e "  ${WHITE}5)${NC} 🏗️ Incremental"
        echo -e "  ${WHITE}6)${NC} ⚡ Fast Build"
        echo -e "  ${WHITE}7)${NC} 🎯 Profiler"
        echo -e "  ${WHITE}8)${NC} 🎭 All Variants"
        echo -e "  ${WHITE}9)${NC} 📱 Multi-arch"
        echo -e "  ${WHITE}10)${NC} 🔄 Rebuild"
        echo ""
        echo -e "${MAGENTA}│${NC}  ${GRAY}0) Назад${NC}"
        echo -e "${MAGENTA}└────────────────────────────────────────┘${NC}"
        echo ""
        echo -en "${CYAN}>${NC} "
        read ch
        
        case $ch in
            1) ./gradlew assembleDebug --no-daemon && echo -e "${GREEN}✓ Done${NC}" ;;
            2) ./gradlew assembleRelease --no-daemon && echo -e "${GREEN}✓ Done${NC}" ;;
            3) ./gradlew clean --no-daemon && echo -e "${GREEN}✓ Done${NC}" ;;
            4) ./gradlew lint --no-daemon && echo -e "${GREEN}✓ Done${NC}" ;;
            5) ./gradlew assembleDebug --no-daemon --incremental && echo -e "${GREEN}✓ Done${NC}" ;;
            6) ./gradlew assembleDebug --no-daemon -x lint && echo -e "${GREEN}✓ Done${NC}" ;;
            7) ./gradlew assembleDebug --no-daemon --profile && echo -e "${GREEN}✓ Done${NC}" ;;
            8) ./gradlew assembleDebug assembleRelease --no-daemon && echo -e "${GREEN}✓ Done${NC}" ;;
            9) ./gradlew assembleDebug --no-daemon && echo -e "${GREEN}✓ Done${NC}" ;;
            10) ./gradlew clean assembleDebug --no-daemon && echo -e "${GREEN}✓ Done${NC}" ;;
            0) break ;;
        esac
        [ "$ch" != "0" ] && read
    done
}

menu_tests() {
    while true; do
        clear
        echo ""
        echo -e "${MAGENTA}┌${WHITE}── Tests ────────────────────────────────┐${NC}"
        echo -e "  ${WHITE}1)${NC} 🧪 Run All Tests"
        echo -e "  ${WHITE}2)${NC} 📊 Test Report"
        echo -e "  ${WHITE}3)${NC} 🔍 Unit Tests"
        echo -e "  ${WHITE}4)${NC} 🎨 UI Tests"
        echo -e "  ${WHITE}5)${NC} ⚡ Performance Test"
        echo -e "  ${WHITE}6)${NC} 🔐 Security Scan"
        echo ""
        echo -e "${MAGENTA}│${NC}  ${GRAY}0) Назад${NC}"
        echo -e "${MAGENTA}└────────────────────────────────────────┘${NC}"
        echo ""
        echo -en "${CYAN}>${NC} "
        read ch
        
        case $ch in
            1) ./gradlew test --no-daemon && echo -e "${GREEN}✓ Passed${NC}" ;;
            2) ./gradlew test --no-daemon && [ -d "app/build/reports/tests" ] && echo -e "${GREEN}✓ Ready${NC}" ;;
            3) ./gradlew testDebugUnitTest --no-daemon && echo -e "${GREEN}✓ Done${NC}" ;;
            4) ./gradlew connectedDebugAndroidTest --no-daemon && echo -e "${GREEN}✓ Done${NC}" ;;
            5) for i in {1..3}; do ./gradlew assembleDebug -q 2>/dev/null; done && echo -e "${GREEN}✓ Done${NC}" ;;
            6) ./gradlew dependencyCheckAnalyze --no-daemon 2>/dev/null && echo -e "${GREEN}✓ OK${NC}" ;;
            0) break ;;
        esac
        [ "$ch" != "0" ] && read
    done
}

menu_apk() {
    while true; do
        clear
        echo ""
        echo -e "${MAGENTA}┌${WHITE}── APK Tools ────────────────────────────┐${NC}"
        echo -e "  ${WHITE}1)${NC} 📊 APK Analyzer"
        echo -e "  ${WHITE}2)${NC} 🔍 Dex Decoder"
        echo -e "  ${WHITE}3)${NC} 📦 AAPT2 Info"
        echo -e "  ${WHITE}4)${NC} 🗜️ Optimizer"
        echo -e "  ${WHITE}5)${NC} 🔐 Signer"
        echo ""
        echo -e "${MAGENTA}│${NC}  ${GRAY}0) Назад${NC}"
        echo -e "${MAGENTA}└────────────────────────────────────────┘${NC}"
        echo ""
        echo -en "${CYAN}>${NC} "
        read ch
        
        case $ch in
            1) ls -lh app/build/outputs/apk/debug/app-debug.apk 2>/dev/null || echo "Build first" ;;
            2) echo "Use apktool to decompile" ;;
            3) aapt dump badging app/build/outputs/apk/debug/app-debug.apk 2>/dev/null | head -10 || echo "Install aapt" ;;
            4) zipalign -v 4 app/build/outputs/apk/debug/app-debug.apk app-aligned.apk 2>/dev/null && echo -e "${GREEN}✓ Optimized${NC}" ;;
            5) jarsigner -keystore ai-app.keystore app/build/outputs/apk/debug/app-debug.apk ai-app 2>/dev/null && echo -e "${GREEN}✓ Signed${NC}" ;;
            0) break ;;
        esac
        [ "$ch" != "0" ] && read
    done
}

menu_device() {
    while true; do
        clear
        echo ""
        echo -e "${MAGENTA}┌${WHITE}── Device ────────────────────────────────┐${NC}"
        echo -e "  ${WHITE}1)${NC} 📲 Install via ADB"
        echo -e "  ${WHITE}2)${NC} 🔌 Install & Run"
        echo -e "  ${WHITE}3)${NC} 📡 ADB Devices"
        echo -e "  ${WHITE}4)${NC} 📳 Logcat"
        echo -e "  ${WHITE}5)${NC} 🔋 Battery"
        echo -e "  ${WHITE}6)${NC} 📱 Device Specs"
        echo -e "  ${WHITE}7)${NC} 🏃 FPS Counter"
        echo -e "  ${WHITE}8)${NC} 🌡️ Temperature"
        echo -e "  ${WHITE}9)${NC} 💾 Memory Info"
        echo -e "  ${WHITE}10)${NC} 📶 Network Test"
        echo ""
        echo -e "${MAGENTA}│${NC}  ${GRAY}0) Назад${NC}"
        echo -e "${MAGENTA}└────────────────────────────────────────┘${NC}"
        echo ""
        echo -en "${CYAN}>${NC} "
        read ch
        
        case $ch in
            1) adb install -r app/build/outputs/apk/debug/app-debug.apk 2>/dev/null && echo -e "${GREEN}✓ Installed${NC}" ;;
            2) adb install -r app/build/outputs/apk/debug/app-debug.apk 2>/dev/null && adb shell am start -n com.aiapp/.MainActivity && echo -e "${GREEN}✓ Launched${NC}" ;;
            3) adb devices ;;
            4) adb logcat -d | tail -20 ;;
            5) adb shell dumpsys battery ;;
            6) echo "Model: $(adb shell getprop ro.product.model 2>/dev/null)" && echo "Android: $(adb shell getprop ro.build.version.release 2>/dev/null)" ;;
            7) adb shell dumpsys gfxinfo com.aiapp 2>/dev/null | grep FPS || echo "Run app first" ;;
            8) adb shell dumpsys thermalservice 2>/dev/null | grep -i temp || echo "No data" ;;
            9) adb shell dumpsys meminfo com.aiapp 2>/dev/null | head -10 ;;
            10) ping -c 2 8.8.8.8 2>/dev/null && echo -e "${GREEN}✓ OK${NC}" ;;
            0) break ;;
        esac
        [ "$ch" != "0" ] && read
    done
}

menu_github() {
    while true; do
        clear
        echo ""
        echo -e "${MAGENTA}┌${WHITE}── GitHub ────────────────────────────────┐${NC}"
        echo -e "  ${WHITE}1)${NC} 🐙 Push"
        echo -e "  ${WHITE}2)${NC} 🌙 Nightly"
        echo -e "  ${WHITE}3)${NC} ⭐ Release"
        echo -e "  ${WHITE}4)${NC} 📋 Create PR"
        echo -e "  ${WHITE}5)${NC} 🔄 Sync Fork"
        echo -e "  ${WHITE}6)${NC} 📊 Git Stats"
        echo -e "  ${WHITE}7)${NC} 📜 Commit Log"
        echo -e "  ${WHITE}8)${NC} 🔀 Branches"
        echo -e "  ${WHITE}9)${NC} 🏷️ Tags"
        echo -e "  ${WHITE}10)${NC} 🗑️ Delete Tag"
        echo -e "  ${WHITE}11)${NC} 🔀 Merge PR"
        echo -e "  ${WHITE}12)${NC} 📃 Issues"
        echo -e "  ${WHITE}13)${NC} ⭐ Star Repo"
        echo ""
        echo -e "${MAGENTA}│${NC}  ${GRAY}0) Назад${NC}"
        echo -e "${MAGENTA}└────────────────────────────────────────┘${NC}"
        echo ""
        echo -en "${CYAN}>${NC} "
        read ch
        
        case $ch in
            1) echo -en "Commit: " && read msg && [ -n "$(git status --porcelain)" ] && git add -A && git commit -m "${msg:-Update}" && git push origin master && echo -e "${GREEN}✓ Pushed${NC}" ;;
            2) echo -en "Commit: " && read msg && [ -n "$(git status --porcelain)" ] && git add -A && git commit -m "${msg:-Nightly}" && git push origin master && echo -e "${GREEN}✓ Done${NC}" ;;
            3) echo -en "Version (v1.0.0): " && read v && [[ "$v" =~ ^v[0-9]+\.[0-9]+\.[0-9]+$ ]] && git tag "$v" && git push origin "$v" && echo -e "${GREEN}✓ Released${NC}" ;;
            4) echo -en "Title: " && read t && gh pr create --title "$t" 2>/dev/null && echo -e "${GREEN}✓ PR created${NC}" ;;
            5) git fetch upstream && git checkout master && git merge upstream/master 2>/dev/null && echo -e "${GREEN}✓ Synced${NC}" ;;
            6) echo "Commits: $(git rev-list --count HEAD) | Files: $(git ls-files | wc -l)" ;;
            7) git log --oneline -5 ;;
            8) git branch -a ;;
            9) git tag -l ;;
            10) echo -en "Tag: " && read t && git tag -d "$t" && git push origin --delete "$t" 2>/dev/null && echo -e "${GREEN}✓ Deleted${NC}" ;;
            11) echo -en "PR #: " && read n && gh pr merge "#$n" 2>/dev/null && echo -e "${GREEN}✓ Merged${NC}" ;;
            12) gh issue list 2>/dev/null | head -5 ;;
            13) gh repo star 2>/dev/null && echo -e "${GREEN}✓ Starred${NC}" ;;
            0) break ;;
        esac
        [ "$ch" != "0" ] && read
    done
}

menu_quality() {
    while true; do
        clear
        echo ""
        echo -e "${MAGENTA}┌${WHITE}── Code Quality ─────────────────────────┐${NC}"
        echo -e "  ${WHITE}1)${NC} 🔎 SpotBugs"
        echo -e "  ${WHITE}2)${NC} ⛔ ErrorProne"
        echo -e "  ${WHITE}3)${NC} 📏 Code Metrics"
        echo -e "  ${WHITE}4)${NC} 🧹 Detekt"
        echo ""
        echo -e "${MAGENTA}│${NC}  ${GRAY}0) Назад${NC}"
        echo -e "${MAGENTA}└────────────────────────────────────────┘${NC}"
        echo ""
        echo -en "${CYAN}>${NC} "
        read ch
        
        case $ch in
            1) ./gradlew spotbugsMain --no-daemon 2>/dev/null && echo -e "${GREEN}✓ OK${NC}" ;;
            2) ./gradlew compileDebugJavaWithJavac --no-daemon 2>&1 | grep -i error | head -3 || echo -e "${GREEN}✓ No errors${NC}" ;;
            3) find app/src -name "*.kt" -exec cat {} \; 2>/dev/null | wc -l ;;
            4) ./gradlew detekt --no-daemon 2>/dev/null && echo -e "${GREEN}✓ OK${NC}" ;;
            0) break ;;
        esac
        [ "$ch" != "0" ] && read
    done
}

menu_files() {
    while true; do
        clear
        echo ""
        echo -e "${MAGENTA}┌${WHITE}── Files ────────────────────────────────┐${NC}"
        echo -e "  ${WHITE}1)${NC} 💾 Backup"
        echo -e "  ${WHITE}2)${NC} 📤 Import"
        echo -e "  ${WHITE}3)${NC} 🗑️ Delete Build"
        echo -e "  ${WHITE}4)${NC} 📂 Open IDE"
        echo -e "  ${WHITE}5)${NC} 📦 Dependencies"
        echo -e "  ${WHITE}6)${NC} 🔍 Find Files"
        echo ""
        echo -e "${MAGENTA}│${NC}  ${GRAY}0) Назад${NC}"
        echo -e "${MAGENTA}└────────────────────────────────────────┘${NC}"
        echo ""
        echo -en "${CYAN}>${NC} "
        read ch
        
        case $ch in
            1) zip -r backup_$(date +%Y%m%d).zip . -x "*.git*" "build/*" "*.apk" && echo -e "${GREEN}✓ Backup done${NC}" ;;
            2) echo -en "File: " && read f && unzip -o "$f" 2>/dev/null && echo -e "${GREEN}✓ Imported${NC}" ;;
            3) ./gradlew clean && rm -rf .gradle build && echo -e "${GREEN}✓ Deleted${NC}" ;;
            4) (idea . &) 2>/dev/null || (code . &) ;;
            5) ./gradlew dependencies --no-daemon 2>&1 | head -15 ;;
            6) echo -en "Query: " && read q && find . -name "*$q*" 2>/dev/null | head -5 ;;
            0) break ;;
        esac
        [ "$ch" != "0" ] && read
    done
}

menu_utils() {
    while true; do
        clear
        echo ""
        echo -e "${MAGENTA}┌${WHITE}── Utils ────────────────────────────────┐${NC}"
        echo -e "  ${WHITE}1)${NC} 📈 HTML Report"
        echo -e "  ${WHITE}2)${NC} 💾 Crash Log"
        echo -e "  ${WHITE}3)${NC} 📉 Error Log"
        echo -e "  ${WHITE}4)${NC} 🔄 Update Gradle"
        echo -e "  ${WHITE}5)${NC} 🧹 Clear Cache"
        echo -e "  ${WHITE}6)${NC} 💾 Toggle Cache"
        echo ""
        echo -e "${MAGENTA}─── Info ───${NC}"
        echo -e "  ${WHITE}7)${NC} 🌳 Dependencies"
        echo -e "  ${WHITE}8)${NC} 📦 APK Size"
        echo -e "  ${WHITE}9)${NC} 🏗️ Build Time"
        echo -e "  ${WHITE}10)${NC} 💻 Java Info"
        echo ""
        echo -e "${MAGENTA}│${NC}  ${GRAY}0) Назад${NC}"
        echo -e "${MAGENTA}└────────────────────────────────────────┘${NC}"
        echo ""
        echo -en "${CYAN}>${NC} "
        read ch
        
        case $ch in
            1) ./gradlew test --no-daemon && echo -e "${GREEN}✓ app/build/reports/tests/index.html${NC}" ;;
            2) { echo "=== Crash ===" && date; } > crash_$(date +%Y%m%d).log && echo -e "${GREEN}✓ Created${NC}" ;;
            3) ./gradlew assembleDebug 2>&1 | grep -i error | tail -5 ;;
            4) ./gradlew wrapper --no-daemon && echo -e "${GREEN}✓ Updated${NC}" ;;
            5) rm -rf ~/.gradle/caches && echo -e "${GREEN}✓ Cache cleared${NC}" ;;
            6) grep -q "caching=true" gradle.properties && sed -i 's/true/false/' gradle.properties && echo "Cache OFF" || echo "org.gradle.caching=true" >> gradle.properties && echo "Cache ON" ;;
            7) ./gradlew dependencies --no-daemon 2>&1 | grep -E "^[+\\\\]---" | head -20 ;;
            8) du -sh app/build/outputs/apk/debug/*.apk 2>/dev/null || echo -e "${RED}No APK${NC}" ;;
            9) time ./gradlew assembleDebug --no-daemon 2>&1 | tail -3 ;;
            10) java -version 2>&1 && echo "JAVA_HOME: $JAVA_HOME" ;;
            0) break ;;
        esac
        [ "$ch" != "0" ] && read
    done
}

while true; do
    clear
    echo ""
    echo -e "${MAGENTA}┌${WHITE}────────────────────────────────────────────┐${NC}"
    echo -e "${MAGENTA}│${NC}     ${CYAN}${BOLD}AI Phone App Builder${NC}                      ${MAGENTA}│${NC}"
    echo -e "${MAGENTA}└────────────────────────────────────────────┘${NC}"
    echo ""
    echo -e "  ${WHITE}B)${NC} 📦 ${CYAN}Build${NC}        ${WHITE}T)${NC} 🧪 ${CYAN}Tests${NC}      ${WHITE}A)${NC} 📱 ${CYAN}APK Tools${NC}"
    echo -e "  ${WHITE}D)${NC} 📱 ${CYAN}Device${NC}     ${WHITE}G)${NC} 🐙 ${CYAN}GitHub${NC}    ${WHITE}Q)${NC} 🔍 ${CYAN}Quality${NC}"
    echo -e "  ${WHITE}F)${NC} 💾 ${CYAN}Files${NC}       ${WHITE}U)${NC} ⚙️ ${CYAN}Utils${NC}"
    echo ""
    echo -e "  ${WHITE}0)${NC} ❌ ${GRAY}Выход${NC}"
    echo ""
    echo -en "${CYAN}>${NC} "
    read choice
    
    case $choice in
        b|B|1) menu_build ;;
        t|T|2) menu_tests ;;
        a|A|3) menu_apk ;;
        d|D|4) menu_device ;;
        g|G|5) menu_github ;;
        q|Q|6) menu_quality ;;
        f|F|7) menu_files ;;
        u|U|8) menu_utils ;;
        0|q|Q) clear; exit 0 ;;
    esac
done
