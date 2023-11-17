import React from 'react';
import { Select } from 'antd';

const CommonSelect = ({ options, placeholder, onSelect, ...otherProps }) => {
	const handleSelect = (value) => {
		onSelect(value);
	};

	return (
		<Select
			placeholder={placeholder}
			onChange={handleSelect}
			options={options}
			style={{ width: 200 }}
		/>
	);
};
export default CommonSelect;
