import React, { useEffect, useState } from "react";
import axios from "axios";
import { apiBaseURL } from "../../../Config.js";
import "./LocationList.styles.css";
import { Link } from "react-router-dom";

export default function LocationList() {

    const [locationList, setLocationList] = useState([]);

    const getLocationList = async () => {
        const response = await axios.get(`${apiBaseURL}/location`);
        setLocationList(response.data.locationList)
    }

    useEffect(() => {
        getLocationList();
    }, []);

    return (
        <>
            {
                locationList.map((location) => {
                    return (
                        <div className="list" key={location.id}>
                            <div className="ui card">
                                <a className="image" href="/">
                                    <Link to={`/home/${location.id}`}>
                                        <img src={location.imageURL} alt={location.name} />
                                    </Link>
                                </a>
                                <div className="content">
                                    <Link to={`/home/${location.id}`}>
                                        <a href="/">{location.name}</a>
                                    </Link>
                                </div>
                            </div>
                        </div>
                    )
                })
            }
        </>
    )
}