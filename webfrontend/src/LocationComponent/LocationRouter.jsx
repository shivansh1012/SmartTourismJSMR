import React, { useState, useContext, useEffect } from "react";
import { Route, useRouteMatch } from "react-router-dom";

import axios from "axios";

import LocationList from "./View/LocationList/LocationList.jsx";
import LocationInfo from "./View/LocationInfo/LocationInfo.jsx";

import BookmarkList from "./View/BookmarkList/BookmarkList.jsx";

import UserAuthContext from "./UserAuthContext.js";

import { mapAPI } from "../Config.js"

import { apiBaseURL } from "../Config.js";

export default function LocationRouter() {
    const [query, setQuery] = useState("");
    const [locationList, setLocationList] = useState([]);
    const [displayList, setDisplayList] = useState([]);

    const { userLoggedIn } = useContext(UserAuthContext);
    const { path } = useRouteMatch();
    const [pointer, setPointer] = useState("India");


    const handleQueryChange = (e) => {
        setQuery(e.target.value);
        setDisplayList(locationList.filter((location) => location['name'].toLowerCase().includes(e.target.value.toLowerCase())));
    }

    useEffect(() => {
        axios.get(`${apiBaseURL}/location`).then((res) => {
            setDisplayList(res.data.locationList);
            setLocationList(res.data.locationList);
        });
    }, [])

    return (
        <>
            <div className="container py-3 w-50">
                <div className="input-group mb-3">
                    <input type="text" className="form-control" placeholder="Search Location"
                        value={query}
                        onChange={handleQueryChange} />
                    <button className="btn btn-outline-secondary" type="button" id="button-addon2">Search</button>
                </div>
            </div>
            <div className="container">
                <div className="d-flex justify-content-between">
                    <div className="leftScroll" style={{ "overflowX": "hidden", "width": "100%" }}>
                        <div>
                            {
                                userLoggedIn === true && (
                                    <>
                                        <Route exact path={`${path}/bookmark`} component={BookmarkList} />
                                    </>
                                )
                            }
                            <Route exact path={`${path}`} component={props => <LocationList locationList={displayList} id={props.match.params.id} />} />
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