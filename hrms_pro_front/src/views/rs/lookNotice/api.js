import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

// 分页获取数据
export const getUserNoticeList = (params) => {
    return getRequest('/userNotice/getByPage', params)
}
// 添加
export const addUserNotice = (params) => {
    return postRequest('/userNotice/insert', params)
}
// 编辑
export const editUserNotice = (params) => {
    return postRequest('/userNotice/update', params)
}
// 删除
export const deleteUserNotice = (params) => {
    return postRequest('/userNotice/delByIds', params)
}