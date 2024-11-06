# alzionlabs

API Documentation for File Upload and Download Service
Base URL
The base URL for all requests is:

bash

http://localhost:9097/api
Endpoints
1. File Upload
POST /files/upload

This endpoint allows clients to upload a file along with a passcode for encryption/decryption or authentication purposes.

Request
Method: POST
URL: http://localhost:9097/api/files/upload
Headers
None required.
Body (Form Data)
The body of the request should be in form-data format, including the following fields:

Key	Type	Description
file	File	The file to be uploaded. This is required.
passcode	String	The passcode for encryption or authentication. This is required.
Example Request Body:
json

{
  "file": "/path/to/your/file/Encryption_Decryption_Info.docx",
  "passcode": "test"
}
Response
Success (HTTP 200 OK):
The server responds with a success message indicating that the file was uploaded successfully.

Response Example:

json

{
  "status": "success",
  "message": "File uploaded successfully"
}
Failure (HTTP 400 or 500):
If an error occurs, the server returns an error message describing the issue.

Response Example:

json

{
  "status": "error",
  "message": "File upload failed due to invalid passcode or server error."
}
2. File Download
GET /files/download/{fileId}

This endpoint allows clients to download a previously uploaded file using its unique file ID. A valid passcode is required for decryption or access.

Request
Method: GET
URL: http://localhost:9097/api/files/download/{fileId}?passcode={passcode}
Replace {fileId} with the unique identifier of the file (UUID) and {passcode} with the passcode used for encryption/decryption or authentication.

Query Parameters
Key	Type	Description
passcode	String	The passcode for decrypting or authenticating the file.
Example Request URL


http://localhost:9097/api/files/download/d6f6b8d5-9cd5-44a8-9cf5-1af035b0f641?passcode=test
Response
Success (HTTP 200 OK):
The server returns the requested file in the response body.

Response Example:

File content (binary stream, e.g., a .docx file)
Failure (HTTP 400 or 401):
If the file ID is invalid or the passcode is incorrect, the server returns an error message.

Response Example:

json

{
  "status": "error",
  "message": "Invalid passcode or file not found."
}
Error Codes
400 Bad Request: The request was invalid (e.g., missing file, invalid parameters).
401 Unauthorized: The passcode provided is incorrect or missing.
404 Not Found: The file ID does not exist or the file is not found.
500 Internal Server Error: A server error occurred while processing the request.
Example Flow
Upload File

The client uploads a file to the server with a passcode (test).
The server processes the upload and returns a success message.
Download File

The client requests the file using the unique file ID and provides the correct passcode.
The server verifies the passcode and returns the file if valid.
