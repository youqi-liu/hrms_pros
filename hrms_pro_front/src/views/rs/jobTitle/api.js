// 统一请求路径前缀在libs/axios.js中修改
import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

// 分页获取数据
export const getJobTitleList = (params) => {
    return getRequest('/jobTitle/getByPage', params)
}
// 添加
export const addJobTitle = (params) => {
    return postRequest('/jobTitle/insertOrUpdate', params)
}
// 编辑
export const editJobTitle = (params) => {
    return postRequest('/jobTitle/insertOrUpdate', params)
}
// 删除
export const deleteJobTitle = (params) => {
    return postRequest('/jobTitle/delByIds', params)
}