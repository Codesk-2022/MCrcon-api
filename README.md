# mc2md-api
mc2md-api is paper plugin for server utilities.

## usage
### POST /chat/send
メッセージを送信します。
#### request
##### header
|name         |value            |
|-------------|-----------------|
|Content-Type |application/json |
##### body
|name     |type |
|---------|-----|
|duration |int  |

#### response
returns same value as request body.

### GET /status/minute
分単位でTPSを取得します。
#### request
##### query params
|name     |type |
|---------|-----|
|duration |int  |

#### response
returns a real number.