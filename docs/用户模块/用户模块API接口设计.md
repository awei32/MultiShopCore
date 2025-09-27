# 用户模块API接口设计文档

## 一、接口设计概述

### 1.1 设计原则

- **RESTful风格**：遵循REST架构风格，使用标准HTTP方法
- **统一响应格式**：所有接口返回统一的JSON格式
- **版本控制**：支持API版本管理，向后兼容
- **安全认证**：JWT Token认证，权限控制
- **参数验证**：严格的输入参数验证和错误处理
- **文档完整**：详细的接口文档和示例

### 1.2 基础信息

| 项目 | 配置 | 说明 |
|------|------|------|
| 基础URL | https://api.multishop.com | 生产环境 |
| 测试URL | https://test-api.multishop.com | 测试环境 |
| API版本 | v1 | 当前版本 |
| 认证方式 | JWT Bearer Token | 请求头Authorization |
| 内容类型 | application/json | 请求和响应格式 |
| 字符编码 | UTF-8 | 统一编码 |

### 1.3 通用响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

### 1.4 状态码定义

| 状态码 | 含义 | 说明 |
|--------|------|------|
| 200 | 成功 | 请求成功处理 |
| 400 | 请求错误 | 参数错误或格式不正确 |
| 401 | 未认证 | 需要登录或Token无效 |
| 403 | 无权限 | 没有访问权限 |
| 404 | 未找到 | 资源不存在 |
| 409 | 冲突 | 资源冲突，如用户名已存在 |
| 429 | 请求过多 | 超过限流阈值 |
| 500 | 服务器错误 | 内部服务器错误 |

## 二、用户认证接口

### 2.1 用户注册

**接口地址：** `POST /api/v1/auth/register`

**接口描述：** 用户注册接口，支持手机号和邮箱注册

**请求参数：**

```json
{
  "registerType": "phone",
  "username": "testuser",
  "password": "123456",
  "phone": "13800138000",
  "email": "test@example.com",
  "captcha": "1234",
  "captchaKey": "captcha_key_123",
  "inviteCode": "INV123456",
  "agreeTerms": true
}
```

**参数说明：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| registerType | string | 是 | 注册类型：phone/email |
| username | string | 是 | 用户名，3-20位字符 |
| password | string | 是 | 密码，6-20位字符 |
| phone | string | 否 | 手机号，registerType为phone时必填 |
| email | string | 否 | 邮箱，registerType为email时必填 |
| captcha | string | 是 | 验证码 |
| captchaKey | string | 是 | 验证码key |
| inviteCode | string | 否 | 邀请码 |
| agreeTerms | boolean | 是 | 是否同意用户协议 |

**响应示例：**

```json
{
  "code": 200,
  "message": "注册成功",
  "data": {
    "userId": 123456,
    "username": "testuser",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "refresh_token_123",
    "expiresIn": 7200
  },
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

### 2.2 用户登录

**接口地址：** `POST /api/v1/auth/login`

**接口描述：** 用户登录接口，支持多种登录方式

**请求参数：**

```json
{
  "loginType": "password",
  "account": "13800138000",
  "password": "123456",
  "captcha": "1234",
  "captchaKey": "captcha_key_123",
  "rememberMe": true
}
```

**参数说明：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| loginType | string | 是 | 登录类型：password/sms/wechat/alipay |
| account | string | 是 | 账号：手机号/邮箱/用户名 |
| password | string | 否 | 密码，password登录时必填 |
| smsCode | string | 否 | 短信验证码，sms登录时必填 |
| captcha | string | 否 | 图形验证码 |
| captchaKey | string | 否 | 验证码key |
| rememberMe | boolean | 否 | 是否记住登录状态 |

**响应示例：**

```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "userId": 123456,
    "username": "testuser",
    "nickname": "测试用户",
    "avatar": "https://cdn.example.com/avatar.jpg",
    "memberLevel": 2,
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "refresh_token_123",
    "expiresIn": 7200
  },
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

### 2.3 刷新Token

**接口地址：** `POST /api/v1/auth/refresh`

**接口描述：** 刷新访问令牌

**请求参数：**

```json
{
  "refreshToken": "refresh_token_123"
}
```

**响应示例：**

```json
{
  "code": 200,
  "message": "Token刷新成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "new_refresh_token_456",
    "expiresIn": 7200
  },
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

### 2.4 用户登出

**接口地址：** `POST /api/v1/auth/logout`

**接口描述：** 用户登出，清除会话信息

**请求头：**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**响应示例：**

```json
{
  "code": 200,
  "message": "登出成功",
  "data": null,
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

### 2.5 第三方登录授权

**接口地址：** `GET /api/v1/auth/oauth/{provider}/authorize`

**接口描述：** 获取第三方登录授权URL

**路径参数：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| provider | string | 是 | 第三方平台：wechat/qq/alipay/weibo |

**请求参数：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| redirectUri | string | 否 | 回调地址 |
| state | string | 否 | 状态参数 |

**响应示例：**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "authorizeUrl": "https://open.weixin.qq.com/connect/oauth2/authorize?appid=xxx&redirect_uri=xxx&response_type=code&scope=snsapi_userinfo&state=xxx",
    "state": "oauth_state_123"
  },
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

### 2.6 第三方登录回调

**接口地址：** `POST /api/v1/auth/oauth/{provider}/callback`

**接口描述：** 处理第三方登录回调，完成登录或绑定

**路径参数：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| provider | string | 是 | 第三方平台：wechat/qq/alipay/weibo |

**请求参数：**

```json
{
  "code": "oauth_code_123",
  "state": "oauth_state_123",
  "action": "login"
}
```

**参数说明：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| code | string | 是 | 授权码 |
| state | string | 是 | 状态参数 |
| action | string | 是 | 操作类型：login/bind |

**响应示例（登录成功）：**

```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "userId": 123456,
    "username": "wx_user_123",
    "nickname": "微信用户",
    "avatar": "https://thirdwx.qlogo.cn/mmopen/xxx",
    "memberLevel": 1,
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "refresh_token_123",
    "expiresIn": 7200,
    "isNewUser": false
  },
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

**响应示例（需要绑定手机号）：**

```json
{
  "code": 1001,
  "message": "需要绑定手机号",
  "data": {
    "oauthToken": "oauth_temp_token_123",
    "userInfo": {
      "openId": "oauth_openid_123",
      "unionId": "oauth_unionid_123",
      "nickname": "微信用户",
      "avatar": "https://thirdwx.qlogo.cn/mmopen/xxx",
      "gender": 1
    }
  },
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

### 2.7 第三方账号绑定手机号

**接口地址：** `POST /api/v1/auth/oauth/bind-phone`

**接口描述：** 第三方登录时绑定手机号完成注册

**请求参数：**

```json
{
  "oauthToken": "oauth_temp_token_123",
  "phone": "13800138000",
  "smsCode": "123456",
  "agreeTerms": true
}
```

**参数说明：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| oauthToken | string | 是 | 第三方临时token |
| phone | string | 是 | 手机号 |
| smsCode | string | 是 | 短信验证码 |
| agreeTerms | boolean | 是 | 是否同意用户协议 |

**响应示例：**

```json
{
  "code": 200,
  "message": "绑定成功",
  "data": {
    "userId": 123456,
    "username": "wx_user_123",
    "nickname": "微信用户",
    "avatar": "https://thirdwx.qlogo.cn/mmopen/xxx",
    "phone": "13800138000",
    "memberLevel": 1,
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "refresh_token_123",
    "expiresIn": 7200,
    "isNewUser": true
  },
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

### 2.8 发送验证码

**接口地址：** `POST /api/v1/auth/captcha/send`

**接口描述：** 发送短信或邮箱验证码

**请求参数：**

```json
{
  "type": "sms",
  "target": "13800138000",
  "scene": "login",
  "captcha": "1234",
  "captchaKey": "captcha_key_123"
}
```

**参数说明：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| type | string | 是 | 验证码类型：sms/email |
| target | string | 是 | 目标手机号或邮箱 |
| scene | string | 是 | 使用场景：register/login/reset/bind |
| captcha | string | 是 | 图形验证码 |
| captchaKey | string | 是 | 验证码key |

**响应示例：**

```json
{
  "code": 200,
  "message": "验证码发送成功",
  "data": {
    "codeKey": "sms_code_key_123",
    "expiresIn": 300,
    "nextSendTime": 60
  },
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

## 三、用户信息管理接口

### 3.1 获取用户信息

**接口地址：** `GET /api/v1/user/profile`

**接口描述：** 获取当前用户详细信息

**请求头：**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**响应示例：**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "userId": 123456,
    "username": "testuser",
    "nickname": "测试用户",
    "avatar": "https://cdn.example.com/avatar.jpg",
    "email": "test@example.com",
    "phone": "13800138000",
    "gender": 1,
    "birthday": "1990-01-01",
    "bio": "这是我的个人简介",
    "memberLevel": 2,
    "memberLevelName": "白银会员",
    "totalAmount": 2500.00,
    "totalPoints": 1000,
    "availablePoints": 800,
    "isEmailVerified": true,
    "isPhoneVerified": true,
    "realNameVerify": {
      "isVerified": true,
      "status": "approved",
      "statusName": "已认证",
      "realName": "张**",
      "submitTime": "2024-01-01 15:30:00"
    },
    "accountSecurity": {
      "securityLevel": "high",
      "securityScore": 85,
      "hasPayPassword": true,
      "twoFactorEnabled": false
    },
    "registerTime": "2023-01-01 10:00:00",
    "lastLoginTime": "2024-01-01 15:30:00"
  },
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

### 3.2 更新用户信息

**接口地址：** `PUT /api/v1/user/profile`

**接口描述：** 更新用户基础信息

**请求参数：**

```json
{
  "nickname": "新昵称",
  "avatar": "https://cdn.example.com/new_avatar.jpg",
  "gender": 1,
  "birthday": "1990-01-01",
  "bio": "个人简介"
}
```

**参数说明：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| nickname | string | 否 | 昵称，2-20位字符 |
| avatar | string | 否 | 头像URL |
| gender | integer | 否 | 性别：0-未知,1-男,2-女 |
| birthday | string | 否 | 生日，格式：YYYY-MM-DD |
| bio | string | 否 | 个人简介，最多200字符 |

**响应示例：**

```json
{
  "code": 200,
  "message": "更新成功",
  "data": {
    "userId": 123456,
    "nickname": "新昵称",
    "avatar": "https://cdn.example.com/new_avatar.jpg",
    "gender": 1,
    "birthday": "1990-01-01",
    "bio": "个人简介"
  },
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

### 3.3 修改密码

**接口地址：** `PUT /api/v1/user/password`

**接口描述：** 修改用户密码

**请求参数：**

```json
{
  "oldPassword": "123456",
  "newPassword": "654321",
  "confirmPassword": "654321"
}
```

**参数说明：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| oldPassword | string | 是 | 原密码 |
| newPassword | string | 是 | 新密码，6-20位字符 |
| confirmPassword | string | 是 | 确认密码 |

**响应示例：**

```json
{
  "code": 200,
  "message": "密码修改成功",
  "data": null,
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

### 3.4 绑定手机号

**接口地址：** `POST /api/v1/user/bind/phone`

**接口描述：** 绑定或更换手机号

**请求参数：**

```json
{
  "phone": "13900139000",
  "smsCode": "123456",
  "codeKey": "sms_code_key_123"
}
```

**响应示例：**

```json
{
  "code": 200,
  "message": "手机号绑定成功",
  "data": {
    "phone": "13900139000"
  },
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

### 3.5 绑定邮箱

**接口地址：** `POST /api/v1/user/bind/email`

**接口描述：** 绑定或更换邮箱

**请求参数：**

```json
{
  "email": "newemail@example.com",
  "emailCode": "123456",
  "codeKey": "email_code_key_123"
}
```

**响应示例：**

```json
{
  "code": 200,
  "message": "邮箱绑定成功",
  "data": {
    "email": "newemail@example.com"
  },
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

### 3.6 获取第三方账号绑定列表

**接口地址：** `GET /api/v1/user/oauth/list`

**接口描述：** 获取当前用户已绑定的第三方账号列表

**请求头：**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**响应示例：**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "id": 1,
      "provider": "wechat",
      "providerName": "微信",
      "openId": "oauth_openid_123",
      "unionId": "oauth_unionid_123",
      "nickname": "微信用户",
      "avatar": "https://thirdwx.qlogo.cn/mmopen/xxx",
      "bindTime": "2024-01-01 10:00:00",
      "lastLoginTime": "2024-01-15 15:30:00",
      "status": "active"
    },
    {
      "id": 2,
      "provider": "qq",
      "providerName": "QQ",
      "openId": "qq_openid_456",
      "unionId": null,
      "nickname": "QQ用户",
      "avatar": "https://q.qlogo.cn/qqapp/xxx",
      "bindTime": "2024-01-05 14:20:00",
      "lastLoginTime": null,
      "status": "active"
    }
  ],
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

### 3.7 绑定第三方账号

**接口地址：** `POST /api/v1/user/oauth/bind`

**接口描述：** 绑定第三方账号到当前用户

**请求头：**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**请求参数：**

```json
{
  "provider": "wechat",
  "code": "oauth_code_123",
  "state": "oauth_state_123"
}
```

**参数说明：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| provider | string | 是 | 第三方平台：wechat/qq/alipay/weibo |
| code | string | 是 | 授权码 |
| state | string | 是 | 状态参数 |

**响应示例：**

```json
{
  "code": 200,
  "message": "绑定成功",
  "data": {
    "id": 3,
    "provider": "wechat",
    "providerName": "微信",
    "openId": "oauth_openid_789",
    "unionId": "oauth_unionid_789",
    "nickname": "新微信用户",
    "avatar": "https://thirdwx.qlogo.cn/mmopen/yyy",
    "bindTime": "2024-01-20 16:45:00",
    "status": "active"
  },
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

### 3.8 解绑第三方账号

**接口地址：** `DELETE /api/v1/user/oauth/{oauthId}`

**接口描述：** 解绑指定的第三方账号

**请求头：**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**路径参数：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| oauthId | integer | 是 | 第三方账号ID |

**响应示例：**

```json
{
  "code": 200,
  "message": "解绑成功",
  "data": null,
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

**错误响应示例：**

```json
{
  "code": 4003,
  "message": "无法解绑，至少需要保留一种登录方式",
  "data": null,
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

### 3.9 实名认证

**接口地址：** `POST /api/v1/user/realname/verify`

**接口描述：** 提交实名认证信息

**实名认证说明：**
- 实名认证后可享受更高的交易限额和安全保障
- 每个身份证号只能认证一个账户
- 认证信息一旦提交不可修改，请确保信息准确
- 认证通常在1-3个工作日内完成审核

**请求参数：**

```json
{
  "realName": "张三",
  "idCard": "110101199001011234",
  "idCardFront": "https://cdn.example.com/idcard_front.jpg",
  "idCardBack": "https://cdn.example.com/idcard_back.jpg",
  "facePhoto": "https://cdn.example.com/face_photo.jpg"
}
```

**参数说明：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| realName | string | 是 | 真实姓名，2-20位中文字符 |
| idCard | string | 是 | 身份证号码，18位 |
| idCardFront | string | 是 | 身份证正面照片URL |
| idCardBack | string | 是 | 身份证反面照片URL |
| facePhoto | string | 是 | 手持身份证照片URL |

**响应示例：**

```json
{
  "code": 200,
  "message": "实名认证信息提交成功",
  "data": {
    "verifyId": "VER202401010001",
    "status": "pending",
    "statusName": "审核中",
    "submitTime": "2024-01-01 15:30:00",
    "estimatedTime": "1-3个工作日"
  },
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

### 3.7 获取实名认证状态

**接口地址：** `GET /api/v1/user/realname/status`

**接口描述：** 获取用户实名认证状态

**响应示例：**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "isVerified": true,
    "verifyId": "VER202401010001",
    "status": "approved",
    "statusName": "已认证",
    "realName": "张**",
    "idCard": "110101********1234",
    "submitTime": "2024-01-01 15:30:00",
    "approveTime": "2024-01-02 10:00:00",
    "rejectReason": null,
    "canResubmit": false,
    "benefits": [
      "提升账户安全等级",
      "享受更高交易限额",
      "优先客服支持",
      "参与高级会员活动"
    ]
  },
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

**状态说明：**

| 状态值 | 状态名称 | 说明 |
|--------|----------|------|
| not_submitted | 未提交 | 尚未提交实名认证 |
| pending | 审核中 | 已提交，等待审核 |
| approved | 已认证 | 审核通过，实名认证成功 |
| rejected | 审核失败 | 审核未通过，可重新提交 |

### 3.8 重新提交实名认证

**接口地址：** `PUT /api/v1/user/realname/resubmit`

**接口描述：** 重新提交实名认证信息（仅在审核失败时可用）

**请求参数：** 同3.6接口

**响应示例：**

```json
{
  "code": 200,
  "message": "实名认证信息重新提交成功",
  "data": {
    "verifyId": "VER202401020001",
    "status": "pending",
    "statusName": "审核中",
    "submitTime": "2024-01-02 15:30:00",
    "estimatedTime": "1-3个工作日"
  },
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

## 四、收货地址管理接口

### 4.1 获取地址列表

**接口地址：** `GET /api/v1/user/addresses`

**接口描述：** 获取用户收货地址列表

**请求参数：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | integer | 否 | 页码，默认1 |
| size | integer | 否 | 每页数量，默认10 |

**响应示例：**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "list": [
      {
        "addressId": 1,
        "receiverName": "张三",
        "receiverPhone": "13800138000",
        "province": "广东省",
        "city": "深圳市",
        "district": "南山区",
        "street": "科技园街道",
        "detailAddress": "科技园南区XX大厦1001室",
        "postalCode": "518000",
        "addressTag": "公司",
        "isDefault": true,
        "createTime": "2023-01-01 10:00:00"
      }
    ],
    "total": 5,
    "page": 1,
    "size": 10
  },
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

### 4.2 添加收货地址

**接口地址：** `POST /api/v1/user/addresses`

**接口描述：** 添加新的收货地址

**请求参数：**

```json
{
  "receiverName": "李四",
  "receiverPhone": "13900139000",
  "province": "北京市",
  "city": "北京市",
  "district": "朝阳区",
  "street": "建国门外大街",
  "detailAddress": "XX商务大厦2001室",
  "postalCode": "100000",
  "addressTag": "公司",
  "isDefault": false
}
```

**参数说明：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| receiverName | string | 是 | 收货人姓名，2-20位字符 |
| receiverPhone | string | 是 | 收货人电话 |
| province | string | 是 | 省份 |
| city | string | 是 | 城市 |
| district | string | 是 | 区县 |
| street | string | 否 | 街道 |
| detailAddress | string | 是 | 详细地址 |
| postalCode | string | 否 | 邮政编码 |
| addressTag | string | 否 | 地址标签 |
| isDefault | boolean | 否 | 是否设为默认地址 |

**响应示例：**

```json
{
  "code": 200,
  "message": "地址添加成功",
  "data": {
    "addressId": 2,
    "receiverName": "李四",
    "receiverPhone": "13900139000",
    "province": "北京市",
    "city": "北京市",
    "district": "朝阳区",
    "street": "建国门外大街",
    "detailAddress": "XX商务大厦2001室",
    "postalCode": "100000",
    "addressTag": "公司",
    "isDefault": false,
    "createTime": "2024-01-01 16:00:00"
  },
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

### 4.3 更新收货地址

**接口地址：** `PUT /api/v1/user/addresses/{addressId}`

**接口描述：** 更新指定的收货地址

**路径参数：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| addressId | integer | 是 | 地址ID |

**请求参数：** 同添加地址接口

**响应示例：**

```json
{
  "code": 200,
  "message": "地址更新成功",
  "data": {
    "addressId": 2,
    "receiverName": "李四",
    "receiverPhone": "13900139000",
    "province": "北京市",
    "city": "北京市",
    "district": "朝阳区",
    "street": "建国门外大街",
    "detailAddress": "XX商务大厦2002室",
    "postalCode": "100000",
    "addressTag": "公司",
    "isDefault": false,
    "updateTime": "2024-01-01 17:00:00"
  },
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

### 4.4 删除收货地址

**接口地址：** `DELETE /api/v1/user/addresses/{addressId}`

**接口描述：** 删除指定的收货地址

**路径参数：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| addressId | integer | 是 | 地址ID |

**响应示例：**

```json
{
  "code": 200,
  "message": "地址删除成功",
  "data": null,
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

### 4.5 设置默认地址

**接口地址：** `PUT /api/v1/user/addresses/{addressId}/default`

**接口描述：** 设置指定地址为默认地址

**路径参数：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| addressId | integer | 是 | 地址ID |

**响应示例：**

```json
{
  "code": 200,
  "message": "默认地址设置成功",
  "data": null,
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

## 五、会员积分接口

### 5.1 获取积分信息

**接口地址：** `GET /api/v1/user/points`

**接口描述：** 获取用户积分信息

**积分规则说明：**
- **积分获取规则：**
  - 购物返积分：消费1元=1积分
  - 签到积分：每日签到获得5积分
  - 评价积分：完成商品评价获得10积分
  - 邀请好友：成功邀请好友注册获得50积分
- **积分使用规则：**
  - 积分抵扣现金：100积分=1元
  - 单笔订单最多可使用积分抵扣50%金额
  - 积分不可转让，仅限本人使用
- **积分有效期：**
  - 积分有效期为2年
  - 到期前1个月系统会发送提醒通知
  - 过期积分将自动清零

**响应示例：**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "totalPoints": 1000,
    "availablePoints": 800,
    "usedPoints": 200,
    "expiredPoints": 50,
    "frozenPoints": 0,
    "memberLevel": 2,
    "memberLevelName": "白银会员",
    "nextLevelPoints": 5000,
    "nextLevelName": "黄金会员",
    "levelProgress": 20.0,
    "pointsExpiringSoon": 100,
    "pointsExpiryDate": "2024-12-31"
  },
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

**响应参数说明：**

| 参数名 | 类型 | 说明 |
|--------|------|------|
| totalPoints | integer | 累计获得积分总数 |
| availablePoints | integer | 当前可用积分 |
| usedPoints | integer | 已使用积分总数 |
| expiredPoints | integer | 已过期积分总数 |
| frozenPoints | integer | 冻结积分（退款等待期） |
| memberLevel | integer | 会员等级：1-普通,2-白银,3-黄金 |
| memberLevelName | string | 会员等级名称 |
| nextLevelPoints | integer | 升级到下一等级所需消费金额 |
| nextLevelName | string | 下一等级名称 |
| levelProgress | decimal | 当前等级进度百分比 |
| pointsExpiringSoon | integer | 即将过期积分（30天内） |
| pointsExpiryDate | string | 最近过期日期 |

### 5.2 获取积分记录

**接口地址：** `GET /api/v1/user/points/logs`

**接口描述：** 获取用户积分变动记录

**请求参数：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pointsType | integer | 否 | 积分类型：1-获得,2-消费,3-过期,4-退还 |
| sourceType | string | 否 | 来源类型：order,signin,review,invite,refund |
| startTime | string | 否 | 开始时间，格式：YYYY-MM-DD |
| endTime | string | 否 | 结束时间，格式：YYYY-MM-DD |
| page | integer | 否 | 页码，默认1 |
| size | integer | 否 | 每页数量，默认10 |

**响应示例：**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "list": [
      {
        "logId": 1,
        "pointsType": 1,
        "pointsTypeName": "获得",
        "pointsAmount": 100,
        "pointsBalance": 900,
        "sourceType": "order",
        "sourceId": "ORD202401010001",
        "sourceTypeName": "订单消费",
        "description": "订单消费获得积分",
        "expireTime": "2026-01-01 15:30:00",
        "createTime": "2024-01-01 15:30:00",
        "remark": "订单号：ORD202401010001，消费金额：100元"
      },
      {
        "logId": 2,
        "pointsType": 1,
        "pointsTypeName": "获得",
        "pointsAmount": 5,
        "pointsBalance": 905,
        "sourceType": "signin",
        "sourceId": null,
        "sourceTypeName": "每日签到",
        "description": "每日签到获得积分",
        "expireTime": "2026-01-02 10:00:00",
        "createTime": "2024-01-02 10:00:00",
        "remark": "连续签到第3天"
      },
      {
        "logId": 3,
        "pointsType": 2,
        "pointsTypeName": "消费",
        "pointsAmount": -50,
        "pointsBalance": 855,
        "sourceType": "order",
        "sourceId": "ORD202401020001",
        "sourceTypeName": "积分抵扣",
        "description": "订单使用积分抵扣",
        "expireTime": null,
        "createTime": "2024-01-02 14:30:00",
        "remark": "抵扣金额：0.5元"
      }
    ],
    "total": 20,
    "page": 1,
    "size": 10,
    "summary": {
      "totalEarned": 1500,
      "totalUsed": 300,
      "totalExpired": 50,
      "currentBalance": 855
    }
  },
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

### 5.3 获取会员等级信息

**接口地址：** `GET /api/v1/user/member/levels`

**接口描述：** 获取所有会员等级信息

**会员等级说明：**
- **普通会员**：累计消费0-999元
- **白银会员**：累计消费1000-4999元  
- **黄金会员**：累计消费5000元以上

**响应示例：**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "levelId": 1,
      "levelCode": "normal",
      "levelName": "普通会员",
      "minAmount": 0,
      "maxAmount": 999,
      "discountRate": 1.00,
      "pointsRate": 1.00,
      "freeShipping": false,
      "birthdayDiscount": null,
      "levelIcon": "https://cdn.example.com/normal.png",
      "levelColor": "#8B4513",
      "description": "享受基础购物服务",
      "privileges": [
        "基础购物权限",
        "标准客服支持",
        "正常积分获取"
      ]
    },
    {
      "levelId": 2,
      "levelCode": "silver",
      "levelName": "白银会员",
      "minAmount": 1000,
      "maxAmount": 4999,
      "discountRate": 0.98,
      "pointsRate": 1.20,
      "freeShipping": false,
      "birthdayDiscount": 0.95,
      "levelIcon": "https://cdn.example.com/silver.png",
      "levelColor": "#C0C0C0",
      "description": "享受更多购物优惠和服务",
      "privileges": [
        "98折购物优惠",
        "1.2倍积分奖励",
        "生日95折优惠",
        "优先客服支持",
        "专属客服热线"
      ]
    },
    {
      "levelId": 3,
      "levelCode": "gold",
      "levelName": "黄金会员",
      "minAmount": 5000,
      "maxAmount": null,
      "discountRate": 0.95,
      "pointsRate": 1.50,
      "freeShipping": true,
      "birthdayDiscount": 0.90,
      "levelIcon": "https://cdn.example.com/gold.png",
      "levelColor": "#FFD700",
      "description": "享受最高级别的购物特权",
      "privileges": [
        "95折购物优惠",
        "1.5倍积分奖励",
        "免费包邮服务",
        "生日9折优惠",
        "VIP专属客服",
        "优先发货服务",
        "专属活动邀请",
        "年度生日礼品"
      ]
    }
  ],
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

### 5.4 积分使用预检

**接口地址：** `POST /api/v1/user/points/check`

**接口描述：** 检查积分是否可用于订单抵扣

**请求参数：**

```json
{
  "orderAmount": 100.00,
  "pointsToUse": 5000
}
```

**参数说明：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderAmount | decimal | 是 | 订单金额 |
| pointsToUse | integer | 是 | 计划使用的积分数量 |

**响应示例：**

```json
{
  "code": 200,
  "message": "检查成功",
  "data": {
    "canUse": true,
    "maxUsablePoints": 5000,
    "maxDeductAmount": 50.00,
    "actualUsablePoints": 5000,
    "actualDeductAmount": 50.00,
    "remainingPoints": 3000,
    "pointsRate": 100,
    "maxDeductRatio": 0.50,
    "tips": "本次最多可使用5000积分，抵扣50元"
  },
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

### 5.5 获取积分到期提醒

**接口地址：** `GET /api/v1/user/points/expiring`

**接口描述：** 获取即将过期的积分信息

**请求参数：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| days | integer | 否 | 查询天数，默认30天 |

**响应示例：**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "totalExpiringPoints": 500,
    "expiringGroups": [
      {
        "expireDate": "2024-01-15",
        "points": 200,
        "daysLeft": 7,
        "sourceType": "order",
        "description": "订单消费积分即将过期"
      },
      {
        "expireDate": "2024-01-20",
        "points": 300,
        "daysLeft": 12,
        "sourceType": "signin",
        "description": "签到积分即将过期"
      }
    ],
    "hasExpiring": true,
    "urgentExpiring": 200
  },
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

## 六、消息通知接口

### 6.1 获取消息列表

**接口地址：** `GET /api/v1/user/messages`

**接口描述：** 获取用户消息列表

**请求参数：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| messageType | string | 否 | 消息类型：system,order,promotion,notice |
| isRead | boolean | 否 | 是否已读 |
| page | integer | 否 | 页码，默认1 |
| size | integer | 否 | 每页数量，默认10 |

**响应示例：**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "list": [
      {
        "messageId": 1,
        "messageType": "order",
        "messageTypeName": "订单消息",
        "title": "订单支付成功",
        "content": "您的订单已支付成功，我们将尽快为您发货。",
        "linkUrl": "/order/detail/123456",
        "isRead": false,
        "priority": 2,
        "priorityName": "中等",
        "createTime": "2024-01-01 15:30:00"
      }
    ],
    "total": 15,
    "unreadCount": 5,
    "page": 1,
    "size": 10
  },
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

### 6.2 标记消息已读

**接口地址：** `PUT /api/v1/user/messages/{messageId}/read`

**接口描述：** 标记指定消息为已读

**路径参数：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| messageId | integer | 是 | 消息ID |

**响应示例：**

```json
{
  "code": 200,
  "message": "标记成功",
  "data": null,
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

### 6.3 批量标记已读

**接口地址：** `PUT /api/v1/user/messages/read/batch`

**接口描述：** 批量标记消息为已读

**请求参数：**

```json
{
  "messageIds": [1, 2, 3, 4, 5],
  "markAll": false
}
```

**参数说明：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| messageIds | array | 否 | 消息ID列表 |
| markAll | boolean | 否 | 是否标记全部未读消息 |

**响应示例：**

```json
{
  "code": 200,
  "message": "批量标记成功",
  "data": {
    "markedCount": 5
  },
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

### 6.4 删除消息

**接口地址：** `DELETE /api/v1/user/messages/{messageId}`

**接口描述：** 删除指定消息

**路径参数：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| messageId | integer | 是 | 消息ID |

**响应示例：**

```json
{
  "code": 200,
  "message": "删除成功",
  "data": null,
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

## 七、用户偏好设置接口

### 7.1 获取偏好设置

**接口地址：** `GET /api/v1/user/preferences`

**接口描述：** 获取用户偏好设置

**响应示例：**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "notifications": {
      "email": true,
      "sms": true,
      "push": true
    },
    "privacy": {
      "profile": true,
      "activity": true
    },
    "system": {
      "language": "zh-CN",
      "timezone": "Asia/Shanghai",
      "currency": "CNY",
      "theme": "light"
    },
    "marketing": {
      "email": true,
      "sms": false
    },
    "security": {
      "autoLogin": false,
      "dataAnalysis": true
    }
  },
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

### 7.2 更新偏好设置

**接口地址：** `PUT /api/v1/user/preferences`

**接口描述：** 更新用户偏好设置

**请求参数：**

```json
{
  "notifications": {
    "email": false,
    "sms": true,
    "push": true
  },
  "privacy": {
    "profile": false,
    "activity": true
  },
  "system": {
    "language": "en-US",
    "timezone": "America/New_York",
    "currency": "USD",
    "theme": "dark"
  },
  "marketing": {
    "email": false,
    "sms": false
  },
  "security": {
    "autoLogin": true,
    "dataAnalysis": false
  }
}
```

**响应示例：**

```json
{
  "code": 200,
  "message": "设置更新成功",
  "data": null,
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

## 八、第三方账号接口

### 8.1 获取绑定账号列表

**接口地址：** `GET /api/v1/user/oauth/accounts`

**接口描述：** 获取已绑定的第三方账号列表

**响应示例：**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "oauthId": 1,
      "oauthType": "wechat",
      "oauthTypeName": "微信",
      "oauthName": "微信用户",
      "oauthAvatar": "https://wx.qlogo.cn/avatar.jpg",
      "bindTime": "2023-01-01 10:00:00",
      "isBound": true
    },
    {
      "oauthId": null,
      "oauthType": "alipay",
      "oauthTypeName": "支付宝",
      "oauthName": null,
      "oauthAvatar": null,
      "bindTime": null,
      "isBound": false
    }
  ],
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

### 8.2 绑定第三方账号

**接口地址：** `POST /api/v1/user/oauth/bind`

**接口描述：** 绑定第三方账号

**请求参数：**

```json
{
  "oauthType": "wechat",
  "authCode": "auth_code_from_wechat",
  "state": "random_state_string"
}
```

**参数说明：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| oauthType | string | 是 | 第三方类型：wechat,alipay,qq,weibo |
| authCode | string | 是 | 第三方授权码 |
| state | string | 是 | 状态参数 |

**响应示例：**

```json
{
  "code": 200,
  "message": "绑定成功",
  "data": {
    "oauthId": 1,
    "oauthType": "wechat",
    "oauthName": "微信用户",
    "oauthAvatar": "https://wx.qlogo.cn/avatar.jpg",
    "bindTime": "2024-01-01 16:00:00"
  },
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

### 8.3 解绑第三方账号

**接口地址：** `DELETE /api/v1/user/oauth/{oauthId}`

**接口描述：** 解绑指定的第三方账号

**路径参数：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| oauthId | integer | 是 | 第三方账号ID |

**响应示例：**

```json
{
  "code": 200,
  "message": "解绑成功",
  "data": null,
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

## 九、用户行为统计接口

### 9.1 获取用户统计信息

**接口地址：** `GET /api/v1/user/statistics`

**接口描述：** 获取用户行为统计信息

**请求参数：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| period | string | 否 | 统计周期：week,month,year，默认month |

**响应示例：**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "period": "month",
    "loginCount": 25,
    "orderCount": 8,
    "totalAmount": 1500.00,
    "pointsEarned": 150,
    "pointsUsed": 50,
    "favoriteCount": 12,
    "reviewCount": 3,
    "activityDays": 20,
    "trends": [
      {
        "date": "2024-01-01",
        "loginCount": 2,
        "orderCount": 1,
        "amount": 200.00
      }
    ]
  },
  "timestamp": 1640995200000,
  "requestId": "req_123456789"
}
```

## 九、安全策略和验证规则

### 9.1 密码安全策略

**密码复杂度要求：**
- 长度：6-20位字符
- 组成：至少包含字母和数字
- 推荐：包含大小写字母、数字和特殊字符
- 禁止：连续相同字符（如：111111）
- 禁止：常见弱密码（如：123456、password）

**密码安全措施：**
- 密码采用BCrypt加密存储，不可逆
- 支持密码强度检测和提示
- 密码错误5次后账号锁定30分钟
- 强制定期更换密码（可选）

### 9.2 登录安全策略

**登录限制：**
- 同一IP地址5分钟内最多尝试登录10次
- 登录失败5次后需要输入图形验证码
- 登录失败10次后IP地址锁定1小时
- 异地登录需要短信验证（可选）

**会话管理：**
- Token有效期：2小时
- RefreshToken有效期：7天
- 支持单点登录控制
- 异常登录自动下线

### 9.3 验证码安全策略

**图形验证码：**
- 有效期：5分钟
- 复杂度：4位数字+字母组合
- 防机器识别：添加干扰线和噪点
- 使用后立即失效

**短信验证码：**
- 有效期：5分钟
- 发送频率：同一手机号1分钟内只能发送1次
- 日发送限制：同一手机号每日最多10次
- 内容加密：验证码采用AES加密存储

### 9.4 数据安全策略

**敏感信息保护：**
- 身份证号：存储时加密，显示时脱敏（如：110101****1234）
- 手机号：显示时脱敏（如：138****8000）
- 真实姓名：显示时脱敏（如：张**）
- 银行卡号：显示时脱敏（如：**** **** **** 1234）

**数据传输安全：**
- 全站HTTPS加密传输
- 敏感接口增加签名验证
- 请求参数校验和过滤
- SQL注入和XSS攻击防护

### 9.5 接口安全策略

**访问控制：**
- JWT Token认证
- 接口权限验证
- 请求频率限制（Rate Limiting）
- IP白名单控制（管理接口）

**参数验证：**
- 严格的参数类型检查
- 参数长度和格式验证
- 特殊字符过滤
- 业务逻辑验证

**防刷机制：**
- 同一用户1秒内最多调用同一接口5次
- 验证码接口1分钟内最多调用3次
- 注册接口同一IP每小时最多调用10次
- 异常请求自动封禁

### 9.6 实名认证安全策略

**认证流程：**
- 三要素验证：姓名+身份证号+人脸识别
- 证件照片OCR识别验证
- 活体检测防止照片欺诈
- 公安部身份信息核验

**信息保护：**
- 认证信息加密存储
- 访问日志记录
- 定期安全审计
- 符合个人信息保护法要求

### 9.7 第三方登录安全策略

**授权安全：**
- OAuth 2.0标准协议
- State参数防CSRF攻击
- 授权码有效期5分钟
- 绑定IP地址验证

**账号安全：**
- 第三方账号与本地账号绑定验证
- 支持解绑和重新绑定
- 异常登录通知
- 定期清理无效绑定

## 十、错误码定义

### 10.1 用户认证相关错误

| 错误码 | 错误信息 | 说明 |
|--------|----------|------|
| 40001 | 用户名或密码错误 | 登录凭证无效 |
| 40002 | 验证码错误或已过期 | 验证码验证失败 |
| 40003 | 账号已被锁定 | 用户状态异常 |
| 40004 | Token已过期 | 需要重新登录 |
| 40005 | Token无效 | Token格式错误或被篡改 |
| 40006 | 用户名已存在 | 注册时用户名冲突 |
| 40007 | 手机号已被注册 | 注册时手机号冲突 |
| 40008 | 邮箱已被注册 | 注册时邮箱冲突 |
| 40009 | 验证码发送过于频繁 | 触发发送限制 |
| 40010 | 登录失败次数过多 | 触发安全限制 |
| 40011 | 第三方登录授权失败 | OAuth授权过程异常 |
| 40012 | 第三方账号已绑定其他用户 | 第三方账号冲突 |
| 40013 | 第三方账号信息获取失败 | 第三方API调用异常 |
| 40014 | 授权码已过期或无效 | OAuth授权码验证失败 |
| 40015 | 需要绑定手机号 | 第三方登录需要补充信息 |

### 10.2 用户信息相关错误

| 错误码 | 错误信息 | 说明 |
|--------|----------|------|
| 40101 | 用户不存在 | 用户ID无效 |
| 40102 | 参数格式错误 | 请求参数不符合要求 |
| 40103 | 手机号格式错误 | 手机号格式验证失败 |
| 40104 | 邮箱格式错误 | 邮箱格式验证失败 |
| 40105 | 昵称长度超限 | 昵称字符数超过限制 |
| 40106 | 密码强度不够 | 密码不符合安全要求 |
| 40107 | 原密码错误 | 修改密码时原密码验证失败 |
| 40108 | 新密码与原密码相同 | 新密码不能与原密码相同 |
| 40109 | 实名认证信息不完整 | 必填字段缺失 |
| 40110 | 身份证号格式错误 | 身份证号码验证失败 |
| 40111 | 身份证号已被认证 | 一个身份证只能认证一个账户 |
| 40112 | 实名认证审核中 | 请等待审核结果 |
| 40113 | 实名认证已通过 | 不能重复认证 |
| 40114 | 实名认证被拒绝 | 认证信息不符合要求 |
| 40115 | 人脸识别失败 | 人脸验证不通过 |
| 40116 | 证件照片不清晰 | 请重新上传清晰照片 |

### 10.3 地址管理相关错误

| 错误码 | 错误信息 | 说明 |
|--------|----------|------|
| 40201 | 地址不存在 | 地址ID无效 |
| 40202 | 地址数量超限 | 超过最大地址数量限制 |
| 40203 | 收货人姓名不能为空 | 必填字段验证失败 |
| 40204 | 收货人电话格式错误 | 电话号码格式验证失败 |
| 40205 | 地址信息不完整 | 省市区信息缺失 |
| 40206 | 详细地址不能为空 | 必填字段验证失败 |
| 40207 | 无法删除默认地址 | 需要先设置其他默认地址 |

### 10.4 积分会员相关错误

| 错误码 | 错误信息 | 说明 |
|--------|----------|------|
| 40301 | 积分余额不足 | 可用积分不够扣除 |
| 40302 | 积分记录不存在 | 积分记录ID无效 |
| 40303 | 会员等级配置错误 | 会员等级数据异常 |
| 40304 | 积分已过期 | 积分超过有效期 |
| 40305 | 积分操作失败 | 积分变动处理异常 |

### 10.5 系统相关错误

| 错误码 | 错误信息 | 说明 |
|--------|----------|------|
| 50001 | 系统繁忙，请稍后重试 | 服务器内部错误 |
| 50002 | 数据库连接失败 | 数据库服务异常 |
| 50003 | 缓存服务异常 | Redis服务异常 |
| 50004 | 第三方服务调用失败 | 外部API调用异常 |
| 50005 | 文件上传失败 | 文件服务异常 |
| 50006 | 请求频率过快 | 触发限流机制 |
| 50007 | IP地址被封禁 | 异常行为检测 |
| 50008 | 签名验证失败 | 请求签名错误 |
| 50009 | 接口权限不足 | 无访问权限 |
| 50010 | 安全验证失败 | 安全检查不通过 |

### 10.6 第三方服务相关错误

| 错误码 | 错误信息 | 说明 |
|--------|----------|------|
| 40401 | 第三方账号绑定失败 | 绑定过程异常 |
| 40402 | 第三方账号解绑失败 | 解绑条件不满足 |
| 40403 | 无法解绑，至少需要保留一种登录方式 | 安全限制 |
| 40404 | 第三方服务暂不可用 | 第三方平台异常 |
| 40405 | 第三方账号不存在 | 绑定记录无效 |

## 十一、接口调用示例

### 11.1 JavaScript示例

```javascript
// 用户登录
async function login(account, password) {
  try {
    const response = await fetch('/api/v1/auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        loginType: 'password',
        account: account,
        password: password
      })
    });
    
    const result = await response.json();
    
    if (result.code === 200) {
      // 保存Token
      localStorage.setItem('token', result.data.token);
      localStorage.setItem('refreshToken', result.data.refreshToken);
      return result.data;
    } else {
      throw new Error(result.message);
    }
  } catch (error) {
    console.error('登录失败:', error);
    throw error;
  }
}

// 获取用户信息
async function getUserProfile() {
  try {
    const token = localStorage.getItem('token');
    const response = await fetch('/api/v1/user/profile', {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });
    
    const result = await response.json();
    
    if (result.code === 200) {
      return result.data;
    } else if (result.code === 401) {
      // Token过期，尝试刷新
      await refreshToken();
      return getUserProfile(); // 重新调用
    } else {
      throw new Error(result.message);
    }
  } catch (error) {
    console.error('获取用户信息失败:', error);
    throw error;
  }
}

// 刷新Token
async function refreshToken() {
  try {
    const refreshToken = localStorage.getItem('refreshToken');
    const response = await fetch('/api/v1/auth/refresh', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        refreshToken: refreshToken
      })
    });
    
    const result = await response.json();
    
    if (result.code === 200) {
      localStorage.setItem('token', result.data.token);
      localStorage.setItem('refreshToken', result.data.refreshToken);
      return result.data;
    } else {
      // 刷新失败，跳转到登录页
      localStorage.removeItem('token');
      localStorage.removeItem('refreshToken');
      window.location.href = '/login';
    }
  } catch (error) {
    console.error('Token刷新失败:', error);
    throw error;
  }
}
```

### 11.2 Java示例

```java
// HTTP客户端工具类
@Component
public class UserApiClient {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${api.base.url}")
    private String baseUrl;
    
    // 用户登录
    public LoginResponse login(LoginRequest request) {
        String url = baseUrl + "/api/v1/auth/login";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<LoginRequest> entity = new HttpEntity<>(request, headers);
        
        try {
            ResponseEntity<ApiResponse<LoginResponse>> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, 
                new ParameterizedTypeReference<ApiResponse<LoginResponse>>() {}
            );
            
            ApiResponse<LoginResponse> apiResponse = response.getBody();
            if (apiResponse.getCode() == 200) {
                return apiResponse.getData();
            } else {
                throw new BusinessException(apiResponse.getMessage());
            }
        } catch (Exception e) {
            log.error("用户登录失败", e);
            throw new BusinessException("登录失败，请稍后重试");
        }
    }
    
    // 获取用户信息
    public UserProfile getUserProfile(String token) {
        String url = baseUrl + "/api/v1/user/profile";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        
        HttpEntity<?> entity = new HttpEntity<>(headers);
        
        try {
            ResponseEntity<ApiResponse<UserProfile>> response = restTemplate.exchange(
                url, HttpMethod.GET, entity,
                new ParameterizedTypeReference<ApiResponse<UserProfile>>() {}
            );
            
            ApiResponse<UserProfile> apiResponse = response.getBody();
            if (apiResponse.getCode() == 200) {
                return apiResponse.getData();
            } else {
                throw new BusinessException(apiResponse.getMessage());
            }
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            throw new BusinessException("获取用户信息失败");
        }
    }
}

// 请求响应对象
@Data
public class ApiResponse<T> {
    private Integer code;
    private String message;
    private T data;
    private Long timestamp;
    private String requestId;
}

@Data
public class LoginRequest {
    private String loginType;
    private String account;
    private String password;
    private String captcha;
    private String captchaKey;
    private Boolean rememberMe;
}

@Data
public class LoginResponse {
    private Long userId;
    private String username;
    private String nickname;
    private String avatar;
    private Integer memberLevel;
    private String token;
    private String refreshToken;
    private Integer expiresIn;
}
```

---

*本文档为用户模块API接口设计规格说明书，涵盖了认证授权、用户管理、地址管理、积分会员、消息通知等核心功能的接口定义。*