import requests from "./request";


// 1.GET     : 用于获取数据
// 2.POST    : 用于提交数据
// 3.PUT     : 更新数据（把所有数据推送到后端）
// 4.PATCH   : 更新数据（只推送修改的数据到后端）
// 5.DELETE  : 删除数据

// 验证用户学习数据
export const reqDataKey = (key) => requests({ url: `/check/${key}`, method: 'get' });






// 个人端接口

// 图片验证码 使用场景: 登录
export const reqCaptchaImg = () => requests({ url: `api/getCaptchaImg`, method: 'get' });
// 邮箱验证码 使用场景: 注册，忘记密码，个人邮箱修改
export const repEmailCaptcha = (email) => requests({ url: `api/sendEmailCaptcha/${email}`, method: 'post' })

// 登录
export const reqLogin = (user) => requests({ url: `api/login`, method: 'post', data: user });
// 退出
export const reqLogout = () => requests({ url: `api/logout`, method: 'post' });
// 注册
export const reqRegister = (user) => requests({ url: `api/register`, method: 'post', data: user ,responseType:'blob'});
// 重置密码
export const reqReset = (password) => requests({ url: `api/resetPwd`, method: 'post', data: password });


//check申请授权

// //check申请授权
// export const requests
// ({ url: `/authorize/${captcha}`, method: 'get' ,data:personId});

export const GetAuthorization = (captcha,data) =>
    requests({
        url: `qwe/authorize/${captcha}`,
        method: 'post',
        data:data
    });

    //同意申请
export const AgreeAuthorization = (id,data) =>
requests({
    url: `qwe/authorize/${id}/agree`,
    method: 'post',
    data:data
});

    //拒绝申请
export const DisagreeGetAuthorization = (id) =>
    requests({
        url: `qwe/authorize/${id}/disagree`,
        method: 'get',
    });

//查询接口
export const SearchInfo = (captcha,data) =>
    requests({
        url: `qwe/check/${captcha}`,
        method: 'post',
        data:data
    });