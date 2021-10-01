import axios from "axios";
import React, { createContext, useEffect, useState } from "react";
import { apiBaseURL } from "../Config.js";

const UserAuthContext = createContext();

function UserAuthContextProvider(props) {
  const [userLoggedIn, setUserLoggedIn] = useState(undefined);
  const [userName, setUserName] = useState(undefined);
  const [userEmail, setUserEmail] = useState(undefined);

  async function getUserLoggedIn() {
    const loggedInRes = await axios.get(`${apiBaseURL}/user/getloggedin`);
    // console.log(loggedInRes.data.userAuth);
    setUserLoggedIn(loggedInRes.data.userAuth);
    setUserName(loggedInRes.data.name);
    setUserEmail(loggedInRes.data.email);
  }

  useEffect(() => {
    getUserLoggedIn();
  }, []);

  return (
    <UserAuthContext.Provider value={{ userLoggedIn, userName, userEmail, getUserLoggedIn }}>
      {props.children}
    </UserAuthContext.Provider>
  );
}

export default UserAuthContext;
export { UserAuthContextProvider };