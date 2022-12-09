import requests from "./request";

// 个人端接口

// 图片验证码 使用场景: 登录
export const reqCaptchaImg = () => requests({ url: `/getCaptchaImg`, method: 'get' });
// 邮箱验证码 使用场景: 注册，忘记密码，个人邮箱修改
export const repEmailCaptcha = (email) => requests({ url: `/sendEmailCaptcha/${email}`, method: 'post' })

// 登录
export const reqLogin = (user) => requests({ url: `/login`, method: 'post', data: user });
// 退出
export const reqLogout = () => requests({ url: `/logout`, method: 'post' });
// 注册
export const reqRegister = (user) => requests({ url: `/register`, method: 'post', data: user ,responseType:'blob'});
// 重置密码
export const reqReset = (password) => requests({ url: `/resetPwd`, method: 'post', data: password });

// 个人基本信息
export const reqBasicInfo = () => requests({ url: `/user/getUserInfo`, method: 'get' })
// 修改个人电话
export const reqUpdatePhone = (phone) => requests({ url: `/user/updatePhone`, method: 'post', data: phone })
// 修改个人邮箱
export const reqUpdateMail = (email) => requests({ url: `/user/updateMail`, method: 'post', data: email })
// 个人修改密码
export const reqUpdatePassword = (pwd) => requests({ url: `/user/updatePwd`, method: 'post', data: pwd })
// 获取首页基本信息
export const reqHomeInfo = () => requests({ url: `/user/getHomePageInfo`, method: 'get' })


// 个人成绩
export const reqEducation = () => requests({ url: `/eduRecord/getAllEduRecord`, method: 'get' });
// 个人上链信息
export const reqUpLinkEdu = () => requests({ url: `/eduRecord/getUplinkData`, method: 'get' });
// 导出个人成绩验证码
export const reqInfoKey = (eduId) => requests({ url: `/eduRecord/shareRecord`, method: 'post', data: eduId });

// 个人成就
export const reqAchieve = () => requests({ url: `/achieveRecord/getAllAchieves`, method: 'get' })
// 个人所学机构
export const reqAlliance = () => requests({ url: `/eduRecord/getUserAlliance`, method: 'get' })
// 添加个人表单
export const reqCertificationForm = (form) => requests({ url: `/achieveRecord/addAchieve`, headers: { 'Content-Type': 'multipart/form-data' }, method: 'post', data: form })
// 获取个人验证通过成就 *接口废弃
// export const reqPassAchieve = () => requests({ url: `/achieveRecord/getPassedAchieve`, method: 'get' });

// 文件下载接口
export const reqDownload = (file) => requests({ url: `/downloadFile`, params: { fileName: file }, responseType: 'blob', method: 'get' });


