import React, { Component } from "react";
import ReactDOM from "react-dom";

class PictureContainer extends Component {

//  constructor() {
//    super();
//
//    this.state = {
//    };
//  }

  render() {
//    const { } = this.state;
    return (
      <div className="picture-view">
        <div className="picture-service-producer">
            <p>PRODUCER</p>
        </div>
        <div className="picture-service-notifier">
            <p>NOTIFIER</p>
        </div>
        <div className="picture-service-persister">
            <p>PERSISTER</p>
        </div>
        <div className="picture-service-reader">
            <p>READER</p>
        </div>
        <div className="picture-kafka">
            <p>KAFKA</p>
        </div>
        <div className="picture-cassandra">
            <p>CASSANDRA</p>
        </div>
      </div>
    );
  }
}

export default PictureContainer;

const wrapper = document.getElementById("picture-div");
wrapper ? ReactDOM.render(<PictureContainer />, wrapper) : false;
