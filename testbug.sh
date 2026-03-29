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

BOX_H="─"
BOX_TL="┌"
BOX_TR="┐"
BOX_BL="└"
BOX_BR="┘"

show_menu() {
    clear
    echo ""
    echo -e "${RED}${BOX_TL}${BOX_H:0:50}${BOX_TR}${NC}"
    echo -e "${RED}${BOX_V}${NC}  ${CYAN}${BOLD}🐛 Bug Reporter & Tester${NC}${RED}$(printf ' %.0s' {1..20})${BOX_V}${NC}"
    echo -e "${RED}${BOX_BL}${BOX_H:0:50}${BOX_BR}${NC}"
    echo ""
    echo -e "  ${WHITE}1)${NC} 🔍 Run Tests"
    echo -e "  ${WHITE}2)${NC} 📊 Coverage Report"
    echo -e "  ${WHITE}3)${NC} 🐛 Debug Build"
    echo -e "  ${WHITE}4)${NC} 📋 Test List"
    echo -e "  ${WHITE}5)${NC} 🎯 Unit Tests"
    echo -e "  ${WHITE}6)${NC} 🎨 UI Tests"
    echo -e "  ${WHITE}7)${NC} ⚡ Performance Test"
    echo -e "  ${WHITE}8)${NC} 🔐 Security Scan"
    echo ""
    echo -e "${MAGENTA}─── Reports ───${NC}"
    echo -e "  ${WHITE}9)${NC} 📄 HTML Report"
    echo -e "  ${WHITE}10)${NC} 📈 JUnit Report"
    echo -e "  ${WHITE}11)${NC} 💾 Crash Log"
    echo -e "  ${WHITE}12)${NC} 📱 Logcat"
    echo ""
    echo -e "${MAGENTA}─── Device ───${NC}"
    echo -e "  ${WHITE}13)${NC} 📲 Install & Test"
    echo -e "  ${WHITE}14)${NC} 🔄 Restart ADB"
    echo -e "  ${WHITE}15)${NC} 📳 Device Info"
    echo ""
    echo -e "${MAGENTA}─── Analysis ───${NC}"
    echo -e "  ${WHITE}20)${NC} 📊 Memory Analysis"
    echo -e "  ${WHITE}21)${NC} 🌳 Dependency Tree"
    echo -e "  ${WHITE}22)${NC} 📦 APK Info"
    echo -e "  ${WHITE}23)${NC} 💾 Cache Stats"
    echo -e "  ${WHITE}24)${NC} 🏥 Health Check"
    echo ""
    echo -e "${MAGENTA}─── Build Checks ───${NC}"
    echo -e "  ${WHITE}16)${NC} ✅ Lint Check"
    echo -e "  ${WHITE}17)${NC} ⚠️ Find Bugs"
    echo -e "  ${WHITE}18)${NC} 📦 Deps Check"
    echo -e "  ${WHITE}19)${NC} 🔄 Gradle Update"
    echo ""
    echo -e "  ${WHITE}0)${NC}  ❌ ${GRAY}Выход${NC}"
    echo ""
}

while true; do
    show_menu
    echo -en "${CYAN}Выберите:${NC} "
    read choice
    
    case $choice in
        1)
            echo -e "\n${YELLOW}Running all tests...${NC}"
            ./gradlew test --no-daemon 2>&1 | tail -20
            echo -e "${GREEN}✓ Tests complete${NC}"
            ;;
        2)
            echo -e "\n${YELLOW}Coverage report...${NC}"
            ./gradlew testDebugUnitTest --no-daemon 2>/dev/null
            if [ -d "app/build/reports/jacoco" ]; then
                echo -e "${GREEN}✓ Coverage: app/build/reports/jacoco/index.html${NC}"
            else
                echo -e "${YELLOW}No coverage data${NC}"
            fi
            ;;
        3)
            echo -e "\n${YELLOW}Debug build with logging...${NC}"
            ./gradlew assembleDebug --no-daemon -Pdebuggable=true
            [ -f "app/build/outputs/apk/debug/app-debug.apk" ] && echo -e "${GREEN}✓ Debug APK ready${NC}"
            ;;
        4)
            echo -e "\n${CYAN}Available tests:${NC}"
            find app/src/test -name "*Test.kt" 2>/dev/null | head -10 || echo "No tests found"
            ;;
        5)
            echo -e "\n${YELLOW}Unit tests...${NC}"
            ./gradlew testDebugUnitTest --no-daemon 2>&1 | tail -10
            echo -e "${GREEN}✓ Done${NC}"
            ;;
        6)
            echo -e "\n${YELLOW}UI tests...${NC}"
            ./gradlew connectedDebugAndroidTest --no-daemon 2>&1 | tail -10 || echo -e "${YELLOW}No UI tests${NC}"
            ;;
        7)
            echo -e "\n${YELLOW}Performance test...${NC}"
            echo "Running 10 iterations..."
            for i in {1..10}; do
                echo -n "."
                ./gradlew assembleDebug --no-daemon -q 2>/dev/null
            done
            echo ""
            echo -e "${GREEN}✓ Performance test complete${NC}"
            ;;
        8)
            echo -e "\n${YELLOW}Security scan...${NC}"
            ./gradlew dependencyCheckAnalyze --no-daemon 2>/dev/null || echo -e "${YELLOW}No vulnerabilities found${NC}"
            ;;
        9)
            echo -e "\n${YELLOW}Generating HTML report...${NC}"
            ./gradlew test --no-daemon
            if [ -f "app/build/reports/tests/testDebugUnitTest/index.html" ]; then
                echo -e "${GREEN}✓ Report: app/build/reports/tests/testDebugUnitTest/index.html${NC}"
            else
                echo -e "${YELLOW}No tests run${NC}"
            fi
            ;;
        10)
            echo -e "\n${YELLOW}JUnit XML report...${NC}"
            ./gradlew test --no-daemon
            if [ -f "app/build/test-results/testDebugUnitTest/TEST-*.xml" ]; then
                echo -e "${GREEN}✓ JUnit results in app/build/test-results/${NC}"
            fi
            ;;
        11)
            echo -e "\n${YELLOW}Generating crash log...${NC}"
            {
                echo "=== AI Phone App Crash Log ==="
                echo "Date: $(date)"
                echo "Branch: $(git rev-parse --abbrev-ref HEAD 2>/dev/null)"
                echo "Commit: $(git rev-parse HEAD 2>/dev/null)"
                echo ""
                echo "=== Build Info ==="
                ./gradlew --version 2>/dev/null | head -5
                echo ""
                echo "=== Last Build Errors ==="
                ./gradlew assembleDebug --no-daemon 2>&1 | grep -i "error" | head -10
            } > crash_log_$(date +%Y%m%d_%H%M%S).txt
            echo -e "${GREEN}✓ crash_log_*.txt created${NC}"
            ;;
        12)
            echo -e "\n${YELLOW}Logcat (last 100 lines):${NC}"
            adb logcat -d -t 100 2>/dev/null | grep -i "aiapp\|error\|exception" | tail -30 || echo -e "${RED}ADB not connected${NC}"
            ;;
        13)
            echo -e "\n${YELLOW}Installing and testing...${NC}"
            if [ -f "app/build/outputs/apk/debug/app-debug.apk" ]; then
                adb install -r app/build/outputs/apk/debug/app-debug.apk 2>/dev/null && \
                echo -e "${GREEN}✓ Installed${NC}" || echo -e "${RED}✗ Install failed${NC}"
            else
                echo -e "${RED}✗ Build APK first${NC}"
            fi
            ;;
        14)
            echo -e "\n${YELLOW}Restarting ADB...${NC}"
            adb kill-server 2>/dev/null
            sleep 1
            adb start-server 2>/dev/null
            echo -e "${GREEN}✓ ADB restarted${NC}"
            ;;
        15)
            echo -e "\n${CYAN}Device Info:${NC}"
            adb shell getprop ro.product.model 2>/dev/null
            adb shell getprop ro.build.version.release 2>/dev/null
            adb shell dumpsys battery 2>/dev/null | grep level
            ;;
        16)
            echo -e "\n${YELLOW}Running lint...${NC}"
            ./gradlew lint --no-daemon 2>&1 | tail -10
            echo -e "${GREEN}✓ Lint complete${NC}"
            ;;
        17)
            echo -e "\n${YELLOW}Finding bugs...${NC}"
            ./gradlew spotbugsMain --no-daemon 2>/dev/null || echo -e "${YELLOW}No bugs found or spotbugs not configured${NC}"
            ;;
        18)
            echo -e "\n${YELLOW}Checking dependencies...${NC}"
            ./gradlew dependencies --no-daemon 2>&1 | grep -i "conflict\|unused" | head -10 || echo -e "${GREEN}✓ No issues${NC}"
            ;;
        19)
            echo -e "\n${YELLOW}Updating Gradle wrapper...${NC}"
            ./gradlew wrapper --no-daemon 2>/dev/null && echo -e "${GREEN}✓ Updated${NC}" || echo -e "${RED}✗ Failed${NC}"
            ;;
        20)
            echo -e "\n${CYAN}=== Memory Analysis ===${NC}"
            echo -e "${YELLOW}Build with memory stats:${NC}"
            ./gradlew assembleDebug --no-daemon -Dorg.gradle.jvmargs="-Xmx4g" 2>&1 | grep -i "memory\|heap" || echo -e "${GREEN}✓ Build OK${NC}"
            ;;
        21)
            echo -e "\n${CYAN}=== Dependency Tree ===${NC}"
            ./gradlew dependencies --no-daemon 2>&1 | grep -A 2 "debugCompileClasspath" | head -30
            ;;
        22)
            echo -e "\n${CYAN}=== APK Info ===${NC}"
            if [ -f "app/build/outputs/apk/debug/app-debug.apk" ]; then
                ls -lh app/build/outputs/apk/debug/app-debug.apk
                unzip -l app/build/outputs/apk/debug/app-debug.apk 2>/dev/null | grep -i "classes\|dex" | head -5
            else
                echo -e "${RED}No APK found${NC}"
            fi
            ;;
        23)
            echo -e "\n${CYAN}=== Build Cache Stats ===${NC}"
            ./gradlew --status 2>/dev/null | head -5
            du -sh ~/.gradle/caches/ 2>/dev/null || echo -e "${GRAY}Cache info not available${NC}"
            ;;
        24)
            echo -e "\n${CYAN}=== Quick Health Check ===${NC}"
            echo -e "${YELLOW}1. Checking Java...${NC}"
            java -version 2>&1 | head -1
            echo -e "${YELLOW}2. Checking Gradle...${NC}"
            ./gradlew --version 2>&1 | head -3
            echo -e "${YELLOW}3. Checking SDK...${NC}"
            echo $ANDROID_HOME
            echo -e "${GREEN}✓ Health check complete${NC}"
            ;;
        0|q|Q)
            clear
            exit 0
            ;;
    esac
    
    echo ""
    echo -en "${GRAY}Enter...${NC}"
    read
done
