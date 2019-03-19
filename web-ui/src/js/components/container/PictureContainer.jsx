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
      <div className="picture-grid">
        <div className="picture-service-producer">
            PRODUCER
        </div>
        <div className="picture-service-notifier">
            NOTIFIER
        </div>
        <div className="picture-service-persister">
            PERSISTER
        </div>
        <div className="picture-service-reader">
            READER
        </div>
        <div className="picture-kafka">
        </div>
        <div className="picture-cassandra">
        </div>
        <div className="picture-arrow-1">
        </div>
        <div className="picture-arrow-2">
        </div>
        <div className="picture-arrow-3">
        </div>
        <div className="picture-arrow-4">
        </div>
        <div className="picture-arrow-5">
        </div>
        <div className="picture-arrow-6">
        </div>
        <div className="picture-arrow-7">
        </div>
        <div className="picture-arrow-8">
        </div>
        <div className="picture-arrow-9">
        </div>
        <div className="picture-arrow-10">
        </div>
      </div>
    );
  }
}

export default PictureContainer;

const wrapper = document.getElementById("picture-div");
wrapper ? ReactDOM.render(<PictureContainer />, wrapper) : false;
