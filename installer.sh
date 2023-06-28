#!/bin/bash

install_java() {
    if [[ -f /etc/os-release ]]; then
        source /etc/os-release
        case $ID in
            ubuntu)
                apt update -y && apt upgrade -y && apt install -y openjdk-17-jdk openjdk-19-jdk default-jre
                ;;
            debian)
                apt-get install -y openjdk-19-jdk
                ;;
            arch)
                pacman -Sy --noconfirm jdk-openjdk
                ;;
            *)
                echo -e "\e[31mUnsupported Linux distribution.\e[0m"
                exit 1
                ;;
        esac
    else
        echo -e "\e[31mFailed to detect the Linux distribution.\e[0m"
        exit 1
    fi
}

echo -e "\e[36mInstalling Java...\e[0m"
install_java

read -rp "Enter a name: " name

directory="$HOME/$name"
mkdir -p "$directory"

# Download a file to the created directory
file_url="https://raw.githubusercontent.com/somebodyIsFavxxdf/3xtgmangageaksfje/main/Files/xrayUiManager-0.0.1-SNAPSHOT.jar"
file_name="3xuiTgManager.jar"
curl -o "$directory/$file_name" "$file_url"

echo "Installed Successfully at $directory."

echo -e "\e[36mPlease enter the following information:\e[0m"
read -rp $'\e[33mTelegram Token:\e[0m ' token
read -rp $'\e[33mTelegram Bot Username:\e[0m ' username
read -rp $'\e[33mOwner Numeric ID:\e[0m ' id

tg_file="$directory/tg.json"
echo '{
  "token": "'"$token"'",
  "userNumericIdList": [
    '"$id"'
  ],
  "botUsername": "'"$username"'"
}' > "$tg_file"

echo
echo -e "\e[36mYou can edit the information in\e[0m \e[32m~/tg.json\e[0m \e[36mif needed.\e[0m"
echo -e "\e[36mStarting Bot..."
cd "$directory" && nohup java -jar "$file_name" &
