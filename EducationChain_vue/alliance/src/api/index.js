import requests from "./request";

// 联盟端接口

// 图片验证码 使用场景: 登录
export const reqCaptchaImg = () => requests({ url: `/getCaptchaImg`, method: 'get' });
// 邮箱验证码 使用场景: 注册，忘记密码，个人邮箱修改
export const repEmailCaptcha = (email) => requests({ url: `/sendEmailCaptcha/${email}`, method: 'post' })

// 登录
export const reqLogin = (user) => requests({ url: `/login`, method: 'post', data: user });
// 退出
export const reqLogout = () => requests({ url: `/logout`, method: 'post' });
// 注册
export const reqRegister = (alliance) => requests({ url: `/alliance/register`, method: 'post', data: alliance });
// 重置密码
export const reqReset = (password) => requests({ url: `/resetPwd`, method: 'post', data: password });

// 个人基本信息
export const reqBasicInfo = () => requests({ url: `/user/getUserInfo`, method: 'get' })
// 修改个人电话
export const reqUpdatePhone = (phone) => requests({ url: `/user/updatePhone`, method: 'post', data: phone })
// 个人修改密码
export const reqUpdatePassword = (pwd) => requests({ url: `/user/updatePwd`, method: 'post', data: pwd })
// 获取首页基本信息
export const reqHomeInfo = () => requests({ url: `/alliance/getHomePageInfo`, method: 'get' })

// 专业
export const reqMajor = () => requests({ url: `/major/getAllMajors`, method: 'get' })
// 增加专业
export const reqAddMajor = (major) => requests({ url: `/major/addMajor`, method: 'post', data: major })
// 删除专业
export const reqDelMajor = (majorId) => requests({ url: `/major/delMajor`, method: 'post', data: majorId })
// 批量增加专业
export const reqAddMajors = (majors) => requests({ url: `/major/addMajors`, method: 'post', data: majors })
// 按教育类型返回专业
export const reqMajorByType = (majorType) => requests({ url: `/major/getMajorByType/${majorType}`, method: 'get' })

// 课程
export const reqCourse = (majorId) => requests({ url: `/course/getAllCourses/${majorId}`, method: 'get' })
// 增加课程
export const reqAddCourse = (course) => requests({ url: `/course/addCourse`, method: 'post', data: course })
// 删除课程
export const reqDelCourse = (courseId) => requests({ url: `/course/delCourse`, method: 'post', data: courseId })
// 修改课程信息
export const reqUpdateCourse = (course) => requests({ url: `/course/updateCourse`, method: 'post', data: course })
// 批量增加课程
export const reqAddCourses = (courses) => requests({ url: `/course/addCourses`, method: 'post', data: courses })
// 向学生添加单个课程
export const reqAddStudentCourse = (course) => requests({ url: `/course/addStudentCourse`, method: 'post', data: course })
// 向学生删除单个课程
export const reqDelStudentCourse = (course) => requests({ url: `/course/delStudentCourse`, method: 'post', data: course })

// 学生
export const reqStudent = () => requests({ url: `/student/getAllStudents`, method: 'get' })
// 增加学生
export const reqAddStudent = (student) => requests({ url: `/student/addStudent`, method: 'post', data: student })
// 删除学生
export const reqDelStudent = (stuId) => requests({ url: `/student/delStudent`, method: 'post', data: stuId })
// 修改学生信息
export const reqUpdateStudent = (student) => requests({ url: `/student/updateStudent`, method: 'post', data: student })
// 批量增加学生
export const reqAddStudents = (students) => requests({ url: `/student/addStudents`, method: 'post', data: students })
// 学生数据上链
export const reqUplink = (students) => requests({ url: `/student/uplink`, method: 'post', data: students })
// 获取学生在联盟内教育id
export const reqStudentId = (personId) => requests({ url: `/student/getStudentId/${personId}`, method: 'get' })

// 学生及其课程 
export const reqStudentEducation = () => requests({ url: `/courseScore/getAllStuAndScore`, method: 'get' })
// 修改学生成绩
export const reqUpdateStudentEducation = (education) => requests({ url: `/courseScore/updateStuScore`, method: 'post', data: education });
// 批量修改学生成绩
export const reqUpdateStudentEducations = (educations) => requests({ url: `/courseScore/importScoreByExcel`, method: 'post', data: educations })

// 成就
export const reqStudentAchieve = () => requests({ url: `/achieveRecord/getAllStudentAchieves`, method: 'get' })
// 修改学生成就
export const reqUpdateAchieve = (achieve) => requests({ url: `/achieveRecord/updateStudentAchieve`, method: 'post', data: achieve });
// 添加学生成就
export const reqAddAchieve = (form) => requests({ url: `/achieveRecord/addStudentAchieve`, method: 'post', data: form })
// 下载文件
export const reqDownloadAchieve = (file) => requests({ url: `/downloadFile/${file}`, responseType: 'blob', method: 'get' });

// 添加证书文件
export const reqCertificate = (data) => requests({ url: `/uploadFile`, method: 'post', data: data })

// 用户
export const reqManager = () => requests({ url: `/manage/getUsers`, method: 'get' })
// 增加用户
export const reqAddManager = (manager) => requests({ url: `/manage/addUser`, method: 'post', data: manager })
// 删除用户
export const reqDelManager = (account) => requests({ url: `/manage/delUser`, method: 'post', data: account })
// 修改用户信息
export const reqUpdateManager = (manager) => requests({ url: `/manage/updateUser`, method: 'post', data: manager })
// 批量增加用户
export const reqAddManagers = (managers) => requests({ url: `/manage/addUsers`, method: 'post', data: managers })
// 重置用户密码
export const reqResetManager = (account) => requests({ url: `/manage/resetUserPwd`, method: 'post', data: account })
