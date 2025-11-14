import { getRequest, postRequest } from '@/libs/axios';

export const getWageBankCardVoList = (params) => {
    return getRequest('/salaryWithdrawal/getAll', params)
}
export const setRosterWageBankNumber = (params) => {
    return postRequest('/wage/edit', params)
}
export const auditWage = (params) => {
    return postRequest('/salaryWithdrawal/audit', params)
}