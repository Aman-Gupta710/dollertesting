import "./App.css";
import "bootstrap/dist/css/bootstrap.min.css";
import { Button } from "reactstrap";
import Base from "./components/Base";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import Login from "./pages/Login";
import Signup from "./pages/Signup";
import About from "./pages/About";
import Services from "./pages/Services";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import Userdashboard from "./pages/user-routes/Userdashboard";
import Privateroute from "./components/Privateroute";
import ProfileInfo from "./pages/user-routes/ProfileInfo";
import PostPage from "./pages/PostPage";
import UserProvider from "./context/UserProvider";
import Categories from "./pages/Categories";
import UpdateBlog from "./pages/UpdateBlog";
import UpdateInfo from "./pages/user-routes/UpdateInfo";
import ChangePassword from "./pages/user-routes/changePassword";
import ResetPassword from "./pages/user-routes/resetPassword";
import ForgetPassword from "./pages/user-routes/forgetPassword";
import VerificationStatus from "./pages/user-routes/verificationStatus";
function App() {
  return (
    <UserProvider>
      <BrowserRouter>
        <ToastContainer
        autoClose={2000}
           position="bottom-center" />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />} />
          <Route path="/about" element={<About />} />
          <Route path="/services" element={<Services />} />
          <Route path="/posts/:postId" element={<PostPage />} />
          <Route path="/categories/:categoryId" element={<Categories />} />
          <Route path="/reset-password" element={<ResetPassword/>}/>
          <Route path="/forget-password/:token" element={<ForgetPassword/>}/>
          <Route path="/verifyRegistration" element={<VerificationStatus/>}/>
          <Route path="/user" element={<Privateroute />}>
            <Route path="dashboard" element={<Userdashboard />} />
            <Route path="profile-info/:userId" element={<ProfileInfo />} />
            <Route path="profile-info/update/:userId" element={<UpdateInfo/>} />
            <Route path="update-blog/:blogId" element={<UpdateBlog />} />
            <Route path="changePassword" element={<ChangePassword />} />
          </Route>
        </Routes>
      </BrowserRouter>
    </UserProvider>
  );
}

export default App;
