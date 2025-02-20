# springminio

Boilerplate Spring Boot project with MiniO Object Storage integration.

Before running the project, you need to have a MiniO server running, you can use Docker or install it from https://min.io/

This project already has `docker-compose` file to run MiniO server.

```yaml
version: '3.8'

services:
  minio:
    image: minio/minio:latest
    container_name: minio
    ports:
      - "19000:9000"  # MinIO API port on host 19000
      - "19001:9001"  # MinIO console port on host 19001
    environment:
      MINIO_ROOT_USER: minioadmin  # Default root user
      MINIO_ROOT_PASSWORD: minioadmin  # Default root password
    volumes:
      - minio_data:/data  # Persistent storage for MinIO data
    command: server /data --console-address ":9001"

volumes:
  minio_data:
```

## Running the project

If you dont have MiniO container running, you can run it using `docker-compose` command

```shell
docker-compose -f docker-compose.yml up
```
You can access console at `http://localhost:19001` with default credentials `minioadmin:minioadmin` to create a bucket

Setup the `application.properties`

```properties
minio.endpoint=<minio-api-host>
minio.access-key=<minio-username>
minio.secret-key=<minio-password>
minio.bucket-name=<minio-bucket>
```

In your IDE run the `@SpringBootApplication` to start the Spring Boot application

## Documentation

By default, the project has baseUrl `http://localhost:8080`

### 1. Upload file 

method: `POST`

url: `{baseUrl}/api/v1/files/upload`

accepts: `multipart/form-data`

```json
{
  "file": "file"
}
```
response: `201 CREATED`

```json
{
    "data": {
        "fileName": "7u94qd.jpg",
        "fileType": "image/jpeg",
        "size": 48003
    },
    "message": "File uploaded with success!"
}
```

### 2. Download file

method: `GET`

url: `{baseUrl}/api/v1/files/download?filename={filename}`

request params: `filename`

response: `200 OK`

content-type: `application/octet-stream`

### 3. Delete file

method: `DELETE`

url: `{baseUrl}/api/v1/files?filename={filename}`

request params: `filename`

response: `200 OK`

```json
{
    "data": null,
    "message": "7u94qd.jpg has deleted with success!"
}
```

If you have **scenario to store url of the file in database**, you can save it with pattern `{miniO-api-host}/{bucket-name}/{filename}` but the bucket must be set to public

![MiniO image](/minio_dashboard.png)