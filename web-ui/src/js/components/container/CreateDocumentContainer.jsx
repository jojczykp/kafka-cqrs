import React, { Component } from "react";
import ReactDOM from "react-dom";
import InputAuthor from "../presentational/InputAuthor.jsx";
import InputText from "../presentational/InputText.jsx";
import InputButton from "../presentational/InputButton.jsx";
import OutputTraffic from "../presentational/OutputTraffic.jsx";

class CreateDocumentContainer extends Component {
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
    alert("Create!")
  }

  render() {
    const { id, author, text, traffic } = this.state;
    return (
      <span>
        <InputAuthor
          id="create-document-author"
          label="Author:"
          value={author}
          handleChange={this.handleChange}
        />
        <InputText
          id="create-document-title"
          label="Text:"
          value={text}
          handleChange={this.handleChange}
        />
        <form id="create-document-form">
          <InputButton
            id="create-document-button"
            value="Create Document"
            handleChange={this.handleChange}
          />
        </form>
        <OutputTraffic
          id="create-document-traffic"
          label="Traffic:"
          value={traffic}
          handleChange={this.handleChange}
        />
      </span>
    );
  }
}

export default CreateDocumentContainer;

const wrapper = document.getElementById("create-document-div");
wrapper ? ReactDOM.render(<CreateDocumentContainer />, wrapper) : false;