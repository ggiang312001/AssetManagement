import React from 'react';
import { Input } from 'antd';

const { Search } = Input;

const CommonSearch = ({ placeholder, onSearch, ...otherProps }) => {
	const handleSearch = (value) => {
		onSearch(value);
	};

	return (
		<Search
			allowClear
			placeholder={placeholder}
			onSearch={handleSearch}
			width={100}
		/>
	);
};

export default CommonSearch;
