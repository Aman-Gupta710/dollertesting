//isLoggedIn=>

export const isLoggedIn = () => {
  let data = localStorage.getItem("data");
  let token = localStorage.getItem("token");

  if (data != null && token!=null) return true;
  else return false;
};

//doLogin=> data=>set to localstorage

export const doLogin = (data,next) => {
  localStorage.setItem("token",JSON.stringify(data.token))
  localStorage.setItem("data", JSON.stringify(data.user));
  next()
};

//doLogout=> remove from localStorage

export const doLogout = (next) => {
  localStorage.removeItem('token')
  localStorage.removeItem("data");
  next()
};

//get currentUser
export const getCurrentUserDetail = () => {
  if (isLoggedIn()) {
    return JSON.parse(localStorage.getItem("data"));
  } else {
    return undefined;
  }
};

export const getToken=()=>{
  if(isLoggedIn()){
    return JSON.parse(localStorage.getItem("token"))
  }else{
    return null;
  }
}
