import React, { Component } from "react";
import ReactDOM from "react-dom";
import InputId from "../presentational/InputId.jsx";
import InputButton from "../presentational/InputButton.jsx";

class DeleteDocumentContainer extends Component {
  constructor() {
    super();
    this.state = {
      seo_title: ""
    };
    this.handleChange = this.handleChange.bind(this);
  }

  handleChange(event) {
    this.setState({ [event.target.id]: event.target.value });
  }

  render() {
    const { seo_title } = this.state;
    return (
      <form id="delete-document-form">
        <InputId
          id="delete-document-id"
          label="Id:"
          value={seo_title}
          handleChange={this.handleChange}
        />
        <InputButton
          id="delete-document-button"
          value="Delete Document"
          handleChange={this.handleChange}
        />
      </form>
    );
  }
}

export default DeleteDocumentContainer;

const wrapper = document.getElementById("delete-document-div");
wrapper ? ReactDOM.render(<DeleteDocumentContainer />, wrapper) : false;
