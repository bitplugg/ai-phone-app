#!/bin/bash

cd "$(dirname "$0")"

export JAVA_HOME=~/jdk/jdk-21.0.2
export PATH=$JAVA_HOME/bin:$PATH

# Colors
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
    echo -e "${MAGENTA}${BOX_TL}${BOX_H:0:50}${BOX_TR}${NC}"
    echo -e "${MAGENTA}${BOX_V}${NC}  ${CYAN}${BOLD}AI Phone App Builder${NC}${MAGENTA}$(printf ' %.0s' {1..22})${BOX_V}${NC}"
    echo -e "${MAGENTA}${BOX_BL}${BOX_H:0:50}${BOX_BR}${NC}"
    echo ""
    echo -e "  ${WHITE}1)${NC} 📦 Build Debug APK"
    echo -e "  ${WHITE}2)${NC} 🚀 Build Release APK"
    echo -e "  ${WHITE}3)${NC} 🧹 Clean Build"
    echo -e "  ${WHITE}4)${NC} 📱 Lint Check"
    echo ""
    echo -e "${MAGENTA}─── GitHub ───${NC}"
    echo -e "  ${WHITE}5)${NC} 🐙 Push (без релиза)"
    echo -e "  ${WHITE}6)${NC} 🌙 Nightly Build (master)"
    echo -e "  ${WHITE}7)${NC} ⭐ Release Build (тег)"
    echo ""
    echo -e "  ${WHITE}0)${NC} ❌ ${GRAY}Выход${NC}"
    echo ""
}

while true; do
    show_menu
    echo -en "${CYAN}Выберите:${NC} "
    read choice
    
    case $choice in
        1)
            echo -e "\n${YELLOW}Building debug APK...${NC}"
            ./gradlew assembleDebug --no-daemon
            if [ -f "app/build/outputs/apk/debug/app-debug.apk" ]; then
                echo ""
                echo -e "${GREEN}✓ APK: app/build/outputs/apk/debug/app-debug.apk${NC}"
                ls -lh app/build/outputs/apk/debug/app-debug.apk
            fi
            ;;
        2)
            echo -e "\n${YELLOW}Building release APK...${NC}"
            ./gradlew assembleRelease --no-daemon
            [ -f "app/build/outputs/apk/release/app-release.apk" ] && echo -e "${GREEN}✓ Done!${NC}"
            ;;
        3)
            echo -e "\n${YELLOW}Cleaning...${NC}"
            ./gradlew clean --no-daemon
            echo -e "${GREEN}✓ Done!${NC}"
            ;;
        4)
            echo -e "\n${YELLOW}Running lint...${NC}"
            ./gradlew compileDebugKotlin --no-daemon
            echo -e "${GREEN}✓ Done!${NC}"
            ;;
        5)
            echo ""
            BRANCH=$(git rev-parse --abbrev-ref HEAD 2>/dev/null)
            echo -e "${CYAN}Ветка:${NC} $BRANCH"
            git status --porcelain | head -5
            echo ""
            echo -en "${CYAN}Коммит:${NC} "
            read msg
            [ -n "$(git status --porcelain)" ] && git add -A && git commit -m "${msg:-Update}"
            git push origin "$BRANCH" 2>/dev/null || echo -e "${RED}Нет remote${NC}"
            echo -e "${GREEN}✓ Готово!${NC}"
            ;;
        6)
            echo ""
            echo -e "${YELLOW}🌙 Nightly Build${NC}"
            echo ""
            BRANCH=$(git rev-parse --abbrev-ref HEAD 2>/dev/null)
            echo -e "${CYAN}Ветка:${NC} $BRANCH"
            echo -e "${MAGENTA}Создаст Pre-release на master${NC}"
            echo ""
            echo -en "${CYAN}Коммит:${NC} "
            read msg
            [ -n "$(git status --porcelain)" ] && git add -A && git commit -m "${msg:-Nightly build}"
            git push origin "$BRANCH" 2>/dev/null || echo -e "${RED}Нет remote${NC}"
            echo -e "${GREEN}✓ Готово! Создаст nightly релиз...${NC}"
            ;;
        7)
            echo ""
            echo -e "${YELLOW}⭐ Release Build${NC}"
            echo ""
            echo -en "${CYAN}Версия (v1.0.0):${NC} "
            read version
            if [[ "$version" =~ ^v[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
                echo ""
                echo -en "${CYAN}Коммит:${NC} "
                read msg
                [ -n "$(git status --porcelain)" ] && git add -A && git commit -m "${msg:-Release $version}"
                git push origin "$BRANCH" 2>/dev/null || true
                git tag "$version" && git push origin "$version"
                echo -e "${GREEN}✓ Тэг $version запушен!${NC}"
                echo -e "${GREEN}✓ Создаст релиз на GitHub...${NC}"
            else
                echo -e "${RED}✗ Формат: v1.0.0${NC}"
            fi
            ;;
        0|q|Q)
            clear
            exit 0
            ;;
    esac
    
    echo ""
    echo -en "${GRAY}Нажмите Enter...${NC}"
    read
done
