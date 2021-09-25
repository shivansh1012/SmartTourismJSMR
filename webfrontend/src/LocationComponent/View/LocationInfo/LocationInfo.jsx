import React, { useEffect, useState, useCallback } from "react";
import axios from "axios";
import { apiBaseURL } from "../../../Config.js";
import { Link } from "react-router-dom";

export default function LocationInfo(props) {
    const [locationInfo, setLocationInfo] = useState([]);
    console.log(props);

    const getLocationInfo = useCallback(async () => {
        await axios.get(`${apiBaseURL}/location?id=${props.id}`).then((res) => {
            setLocationInfo(res.data.locationList)
            props.setPointer(res.data.locationList.name);
            // console.log(res)
        });
    }, [props]);

    useEffect(() => {
        getLocationInfo();
    }, [getLocationInfo]);

    return (
        <div className="container p-4">
            <div className="row">
                <div id="back">
                    <Link to="/home" style={{}}>{'<'}Back</Link>
                </div>
                {/*<div className="d-flex justify-content-around mb-3">*/}
                {/*    <h3 className="d-flex flex-wrap align-items-center" style={{ textAlign: "center" }}>{locationInfo.name}</h3>*/}
                {/*    <img src={locationInfo.imageURL} style={{ width: "15rem" }} alt={locationInfo.name}></img>*/}
                {/*</div>*/}
                <div className="location-content">
                    <h3 className="d-flex justify-content-center" style={{ "font-family": "Poppins", "color": "#043263FF"}}>{locationInfo.name}</h3>
                    <img src={locationInfo.imageURL} alt={locationInfo.name} style={{ "width": "100%" }} />
                </div>

                <p>{locationInfo.description}</p>
            </div>
        </div>
    )
}