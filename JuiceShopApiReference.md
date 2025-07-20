# Juice Shop API Reference

All requests require the following headers:
- Content-Type: application/json


## POST /rest/user/login
**Auth:** No Auth

**Example Request Body:**
```json
{ 
  "email": "test@test.com", 
  "password": "abc123"
}
```

**Success:** 200

**Example Response Body:**
```json
{
  "authentication": {
    "token": "eyJ0eXAiOiJKV1QiLCJhbGc...",
    "bid": 6,
    "umail": "aaa@aaa.com"
  }
}
```

## GET /rest/products/search?q=
**Auth:** No Auth

**URL Params**
- q: search term

**Success:** 200

**Example Response Body:**
```json
{
  "status": "success",
  "data": [
    {
      "id": 1,
      "name": "Apple Juice (1000ml)",
      "description": "The all-time classic.",
      "price": 1.99,
      "deluxePrice": 0.99,
      "image": "apple_juice.jpg",
      "createdAt": "2022-01-20 22:18:37.733 +00:00",
      "updatedAt": "2022-01-20 22:18:37.733 +00:00",
      "deletedAt": null
    },
    ...
  ]
}
```

## PUT /rest/products/{product-id}/reviews
**Auth:** Bearer Token

**URL Properties:**
- product-id:  The Unique product identifier

**Example Request Body:**
```json
{
  "message": "This product is the best.",
  "author": "aaa@aaa.com"
}
```

**Success:** 201

**Example Response Body:**
```json
{
  "staus": "success"
}
```

## GET /rest/products/{product-id}/reviews
**Auth:** No Auth

**URL Properties:**
- product-id:  The Unique product identifier

**Success:** 200

**Example Response Body:**
```json
{
  "status": "success",
  "data": [
    {
      "message": "One of my favorites!",
      "author": "admin@juice-sh.op",
      "product": 1,
      "likesCount": 0,
      "likedBy": [],
      "_id": "wpTcPfd4LpCs9yMDr",
      "liked": true
    },
    ...
  ]
}
```