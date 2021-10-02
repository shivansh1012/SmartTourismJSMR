import React, { useState, useContext } from "react";
import { Route, useRouteMatch } from "react-router-dom";

import LocationList from "./View/LocationList/LocationList.jsx";
import LocationInfo from "./View/LocationInfo/LocationInfo.jsx";

import BookmarkList from "./View/BookmarkedList/BookmarkList.jsx";

import UserAuthContext from "./UserAuthContext.js";

import { mapAPI } from "../Config.js"

export default function LocationRouter() {
    const { userLoggedIn } = useContext(UserAuthContext);
    const { path } = useRouteMatch();
    const [pointer, setPointer] = useState("India");
    return (
        <>
            <div className="container py-3">
                <div className="input-group mb-3">
                    <input type="text" className="form-control" placeholder="Search Location" />
                    <button className="btn btn-outline-secondary" type="button" id="button-addon2">Search</button>
                </div>
            </div>
            <div className="container">
                <div className="d-flex justify-content-around">
                    <div className="leftScroll" style={{ "overflowX": "hidden" }}>
                        <div style={{ width: "620px" }}>
                            {
                                userLoggedIn === true && (
                                    <>
                                        <Route exact path={`${path}`} component={LocationList} />
                                        <Route exact path={`${path}/bookmark`} component={BookmarkList} />
                                    </>
                                )
                            }
                            {
                                (userLoggedIn === false || userLoggedIn === undefined) && (
                                    <Route exact path={`${path}`} component={LocationList} />
                                    )
                                }
                            <Route exact path={`${path}/location/:id`} component={props => <LocationInfo setPointer={setPointer} id={props.match.params.id} />} />
                        </div>
                    </div>
                    <div className="d-none d-lg-block">
                        <iframe title="Map" width="600px" height="100%" loading="lazy"
                            src={`https://www.google.com/maps/embed/v1/place?key=${mapAPI}&q=${pointer}`}>
                        </iframe>
                    </div>
                </div>
            </div>
        </>
    );
}