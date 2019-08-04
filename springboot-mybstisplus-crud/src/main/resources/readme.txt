1.创建数据库和表 oracle版本  
执行table.sql
2.启动项目，访问http://localhost:8080/swagger-ui.html 
3.测试接口insert数据 
userDTO中输入
{
  "email": "13537@qq.com",
  "mobile": "1861",
  "page": 0,
  "pageSize": 0,
  "password": "123456",
  "status": "1",
  "userid": "",
  "username": "张三"
}
点击 try it out ! 添加数据

query ,update,delete雷同

4.query 接口返回值
{
  "code": 200,
  "message": null,
  "total": 2,
  "pageSize": 10,
  "data": [
    {
      "userid": "80",
      "username": "李四",
      "password": "123456",
      "email": "13537@qq.com",
      "mobile": null,
      "status": null
    },
    {
      "userid": "81",
      "username": "张三",
      "password": "123456",
      "email": "13537@qq.com",
      "mobile": null,
      "status": null
    }
  ]
}
5.其他接口返回值
{
  "code": 200,
  "message": null,
  "total": 0,
  "pageSize": 10,
  "data": null
}


