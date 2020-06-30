import React, { Component } from "react";
import ReactDOM from "react-dom";

class PictureContainer extends Component {

  static activateFlow(flowClassName) {
      var pictureElements = document.getElementsByClassName('picture-grid')[0].getElementsByTagName('*');

      for (var i = 0 ; i < pictureElements.length ; i++) {
          var e = pictureElements[i];

          if (e.classList.contains(flowClassName)) {
              e.classList.remove('picture-inactive');
              e.classList.add('picture-active');
          } else {
              e.classList.remove('picture-active');
              e.classList.add('picture-inactive');
          }
      }
  }

  render() {
    return (
      <div className="picture-grid">
        <div className="picture-service-producer flow-create flow-update flow-delete">
            PRODUCER
        </div>
        <div className="picture-service-notifier flow-create flow-update flow-delete">
            NOTIFIER
        </div>
        <div className="picture-service-persister flow-create flow-update flow-delete">
            PERSISTER
        </div>
        <div className="picture-service-reader flow-read">
            READER
        </div>
        <div className="picture-kafka flow-create flow-update flow-delete">
        </div>
        <div className="picture-cassandra flow-create flow-update flow-delete flow-read">
        </div>
        <div className="picture-arrow-1 flow-create">
        </div>
        <div className="picture-arrow-2 flow-delete">
        </div>
        <div className="picture-arrow-3 flow-update">
        </div>
        <div className="picture-arrow-4 flow-create flow-update flow-delete">
        </div>
        <div className="picture-arrow-5 flow-create flow-update flow-delete">
        </div>
        <div className="picture-arrow-6 flow-create flow-update flow-delete">
        </div>
        <div className="picture-arrow-7 flow-create flow-update flow-delete">
        </div>
        <div className="picture-arrow-8 flow-create flow-update flow-delete">
        </div>
        <div className="picture-arrow-9 flow-read">
        </div>
        <div className="picture-arrow-10 flow-read">
        </div>
      </div>
    );
  }
}

export default PictureContainer;

const wrapper = document.getElementById("picture-div");
wrapper ? ReactDOM.render(<PictureContainer />, wrapper) : false;
