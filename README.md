# document-flow
ER-диаграмма:
![This is an image](https://i.postimg.cc/vBLDgNG7/ER.png)

Для изменения конфигурации на сохранение файла в minio необходимо изменить 
значение константы service.file в application.properties на minio

## Запуск через Docker

---

Для Windows: 
1. скачать и установить [Docker Desktop](https://www.docker.com/products/docker-desktop/)
2. Запустить Docker Desktop

Для Linux:
1. Скачать последний релиз [Docker Compose](https://github.com/docker/compose/releases)
2. Установить Docker Compose:
```bash
# Add Docker's official GPG key:
sudo apt-get update
sudo apt-get install ca-certificates curl gnupg
sudo install -m 0755 -d /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
sudo chmod a+r /etc/apt/keyrings/docker.gpg

# Add the repository to Apt sources:
echo \
  "deb [arch="$(dpkg --print-architecture)" signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  "$(. /etc/os-release && echo "$VERSION_CODENAME")" stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt-get update
sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin  docker-compose
```

Для запуска проекта необходимо выполнить команду:
```bash
docker-compose up
```
Команда должна быть выполнена в корневой директории проекта (там, где находится файл docker-compose.yml)

В **IntelliJ IDEA** для запуска необходимо нажать по файлу Dockerfile правой кнопкой мыши и выбрать пункт `Run 'Dockerfile'`

**Пароль от админа:** 251323Nn


## Как снять логи с контейнера

Заходим в Docker Desktop, выбираем контейнер, с которого хотим снять логи, нажимаем на кнопку `Logs`
Перед вами должна появиться консоль с логами контейнера
![image](https://github.com/Toolll1/document-flow/assets/23121394/e06ff6de-5d97-4d6a-94f3-a49ec19f37cd)

Копируем всё что находится в консоли и сохраняем в файл
