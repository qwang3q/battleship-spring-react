const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');

export default class Cell extends React.Component {
    render() {
        const char = this.props.cell.shipCell == true ?
             "S"
        :
            "";
        return (
            <th>
                {char}
            </th>
        )
    }
}
