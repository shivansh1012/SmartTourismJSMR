//Modules
import React from "react";
import { BrowserRouter, Switch, Route, Redirect } from "react-router-dom";

import NavBar from "./LocationComponent/Layout/NavBar.jsx";
import LocationRouter from "./LocationComponent/LocationRouter.jsx";
import NewLocation from "./LocationComponent/View/NewLocation/NewLocation.jsx";

export default function App() {
  return (
    <BrowserRouter>
      <NavBar />
      <Switch>
        <Route path="/home" component={LocationRouter} />
        <Route path="/new" component={NewLocation} />

        <Route render={() => <Redirect to={{ pathname: "/home" }} />} />
      </Switch>
    </BrowserRouter>
  );
}