import React from "react";
import "./LocationList.styles.css";
import { Link } from "react-router-dom";

export default function LocationList(props) {

    return (
        <>
            <div className="container">
                <p className="pheading">Trending Locations</p>
                {
                    props.locationList.map((location) => {
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