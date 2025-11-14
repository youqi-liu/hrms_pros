import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

// 分页获取数据
export const getUserLeaveList = (params) => {
    return getRequest('/userLeave/getMyByPage', params)
}
// 添加
export const addUserLeave = (params) => {
    return postRequest('/userLeave/insert', params)
}
// 编辑
export const editUserLeave = (params) => {
    return postRequest('/userLeave/update', params)
}
// 删除
export const deleteUserLeave = (params) => {
    return postRequest('/userLeave/delByIds', params)
}