//Modules
import React from "react";
import { BrowserRouter, Switch, Route, Redirect } from "react-router-dom";

import NavBar from "./LocationComponent/Layout/NavBar.jsx";
import LocationRouter from "./LocationComponent/LocationRouter.jsx";
import NewLocation from "./LocationComponent/View/NewLocation/NewLocation.jsx";
import UserLogin from "./LocationComponent/View/UserLogin/UserLogin.jsx";
import UserRegister from "./LocationComponent/View/UserRegister/UserRegister.jsx";
import { UserAuthContextProvider } from "./LocationComponent/UserAuthContext.js";

import axios from "axios";
axios.defaults.withCredentials = true;

export default function App() {
  return (
    <BrowserRouter>
      <UserAuthContextProvider>
        <NavBar />
        <Switch>
          <Route path="/home" >
            <LocationRouter />
          </Route>
          <Route path="/login" component={UserLogin} />
          <Route path="/register" component={UserRegister} />

          <Route path="/new" component={NewLocation} />

          <Route render={() => <Redirect to={{ pathname: "/home" }} />} />
        </Switch>
      </UserAuthContextProvider>
    </BrowserRouter>
  );
}