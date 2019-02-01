import React, { Component } from "react";
import ReactDOM from "react-dom";
import InputId from "../presentational/InputId.jsx";
import InputButton from "../presentational/InputButton.jsx";
import OutputTraffic from "../presentational/OutputTraffic.jsx";

class DeleteDocumentContainer extends Component {
  constructor() {
    super();
    this.state = {
      id: "",
      traffic: ""
    };
    this.handleChange = this.handleChange.bind(this);
  }

  handleChange(event) {
//    this.setState({ [event.target.id]: event.target.value });
    alert("Delete! " + event.target.id + " " + event.target.value)
  }

  onClick(event) {
    alert("Delete! click " + event.target.id + " " + event.target.value)
  }

  render() {
    const { id, traffic } = this.state;
    return (
      <span>
        <form id="delete-document-form">
          <InputId
            id="delete-document-id"
            label="Id:"
            value={id}
            handleChange={this.handleChange}
          />
          <InputButton
            id="delete-document-button"
            value="Delete Document"
            onClick={this.onClick}
          />
        </form>
        <OutputTraffic
          id="delete-document-traffic"
          label="Traffic:"
          value={traffic}
        />
      </span>
    );
  }
}

export default DeleteDocumentContainer;

const wrapper = document.getElementById("delete-document-div");
wrapper ? ReactDOM.render(<DeleteDocumentContainer />, wrapper) : false;
