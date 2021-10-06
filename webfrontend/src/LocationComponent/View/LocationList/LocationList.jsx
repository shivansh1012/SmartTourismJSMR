import React, { useEffect, useState } from "react";
import axios from "axios";
import { apiBaseURL } from "../../../Config.js";
import "./LocationList.styles.css";
import { Link } from "react-router-dom";

export default function LocationList() {

    const [locationList, setLocationList] = useState([]);

    const getLocationList = async () => {
        const response = await axios.get(`${apiBaseURL}/location`);
        // console.log(response.data.locationList);
        setLocationList(response.data.locationList)
    }

    useEffect(() => {
        getLocationList();
    }, []);

    return (
        <>
            <div className="container px-5">
                <p style={{ "textAlign": "center", "fontSize": "25px"}}>Trending Locations</p>
                {
                    locationList.map((location) => {
                        return (
                            <div className="list" key={location.id}>
                                <div className="card">
                                    <Link to={`/home/location/${location.id}`}>
                                        <img src={location.imageURL} alt={location.name} className="p-3" />
                                    </Link>
                                    <div className="content text-center">
                                        <Link to={`/home/location/${location.id}`}>
                                            {location.name}
                                        </Link>
                                    </div>
                                </div>
                            </div>
                        )
                    })
                }
            </div>
        </>
    )
}