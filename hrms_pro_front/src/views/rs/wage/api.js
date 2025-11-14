import { getRequest, postRequest } from '@/libs/axios';

export const getWageBankCardVoList = (params) => {
    return getRequest('/wage/getByPage', params)
}
export const setRosterWageBankNumber = (params) => {
    return postRequest('/wage/edit', params)
}