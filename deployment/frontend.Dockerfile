FROM ubuntu:latest
RUN apt-get update && apt-get install npm -y
COPY frontend ./frontend
COPY start_frontend.sh ./start_frontend.sh
WORKDIR /frontend/my-app

CMD ["/start_frontend.sh"]
