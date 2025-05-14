FROM golang:1.21.4-alpine

WORKDIR /product-v1

COPY . .
RUN go build -o product-v1

EXPOSE 8017
CMD ["./product-v1"]