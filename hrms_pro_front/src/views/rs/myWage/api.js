import { getRequest, postRequest } from '@/libs/axios';

export const getWageBankCardVoList = (params) => {
    return getRequest('/wage/getByPage', params)
}
export const setRosterWageBankNumber = (params) => {
    return postRequest('/wage/edit', params)
}
export const getMyWage = (params) => {
    return getRequest('/user/getMyWage', params)
}
export const getMyZwzInfo = (params) => {
    return getRequest('/salaryWithdrawal/getMyInfo', params)
}
export const applyWage = (params) => {
    return postRequest('/salaryWithdrawal/apply', params)
}
export const findWage = (params) => {
    return postRequest('/salaryWithdrawal/find', params)
}