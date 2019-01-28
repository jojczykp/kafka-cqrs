import React, { Component } from "react";
import ReactDOM from "react-dom";
import InputButton from "../presentational/InputButton.jsx";

class CreateDocumentContainer extends Component {
  constructor() {
    super();
    this.handleChange = this.handleChange.bind(this);
  }

  handleChange(event) {
    alert("Create!")
  }

  render() {
    return (
      <form id="create-document-form">
        <InputButton
          id="create-document-button"
          value="Create Document"
          handleChange={this.handleChange}
        />
      </form>
    );
  }
}

export default CreateDocumentContainer;

const wrapper = document.getElementById("create-document-div");
wrapper ? ReactDOM.render(<CreateDocumentContainer />, wrapper) : false;
