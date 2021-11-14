import React from "react";
import "./LocationList.styles.css";
import { Link } from "react-router-dom";

export default function LocationList(props) {

    return (
        <>
            <div className="container">
                <p className="pheading">Trending Locations</p>
                {
                    props.locationList.sort((location1, location2) => {
                        if(location1.views<location2.views) return 1
                        else if(location1.views===location2.views) return 0
                        else return -1
                    }).map((location) => {
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