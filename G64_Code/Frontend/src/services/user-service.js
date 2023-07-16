import { myAxios,privateAxios  } from "./helper";

export const signUp = (user) => {
  return myAxios
    .post("/auth/register", user)
    .then((response) => response.data);
};

export const loginUser = (loginDetail) => {
  return myAxios.post('/auth/login', loginDetail).then((response) => response.data)
}

export const updateUser=(updateDetails,userId)=>{
  return privateAxios.put(`/users/${userId}`,updateDetails).then(resp => resp.data)
}

export const getUser = (userId) => {
  return myAxios.get(`/users/${userId}`).then(resp => resp.data)
};

export const changePassword=(details)=>{
  return myAxios.post(`/auth/changePassword`,details).then(resp => resp.data)
}

export const sendResetEmail=(details)=>{
  return myAxios.post(`/auth/resetPassword`,details).then(resp => resp.data)
}

export const resetPassword=(details,token)=>{
  return myAxios.post(`/auth/savePassword?token=${token}`,details).then(resp => resp.data)
}

export const verifyToken=(token)=>{
  return myAxios.get(`/auth/verifyRegistration?token=${token}`).then((res)=>res.data)
}