import React, { Component } from "react";
import ReactDOM from "react-dom";
import InputId from "../presentational/InputId.jsx";
import InputAuthor from "../presentational/InputAuthor.jsx";
import InputText from "../presentational/InputText.jsx";
import InputButton from "../presentational/InputButton.jsx";
import OutputTraffic from "../presentational/OutputTraffic.jsx";

class UpdateDocumentContainer extends Component {
  constructor() {
    super();
    this.state = {
      id: "",
      author: "",
      text: "",
      traffic: ""
    };
    this.handleChange = this.handleChange.bind(this);
  }

  handleChange(event) {
//    this.setState({ [event.target.id]: event.target.value });
    alert("Update! " + event.target.id + " " + event.target.value)
  }

  onClick(event) {
    alert("Update! click " + event)
  }

  render() {
    const { id, author, text, traffic } = this.state;
    return (
      <span>
        <form id="update-document-form">
          <InputId
            id="update-document-id"
            label="Id:"
            value={id}
            handleChange={this.handleChange}
          />
          <InputAuthor
            id="update-document-author"
            label="Author:"
            value={author}
            handleChange={this.handleChange}
          />
          <InputText
            id="update-document-title"
            label="Text:"
            value={text}
            handleChange={this.handleChange}
          />
          <InputButton
            id="update-document-button"
            value="Update Document"
            onClick={this.onClick}
          />
        </form>
        <OutputTraffic
          id="update-document-traffic"
          label="Traffic:"
          value={traffic}
        />
      </span>
    );
  }
}

export default UpdateDocumentContainer;

const wrapper = document.getElementById("update-document-div");
wrapper ? ReactDOM.render(<UpdateDocumentContainer />, wrapper) : false;
