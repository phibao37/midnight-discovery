<?php

return [

	/*
	|--------------------------------------------------------------------------
	| Validation Language Lines
	|--------------------------------------------------------------------------
	|
	| The following language lines contain the default error messages used by
	| the validator class. Some of these rules have multiple versions such
	| as the size rules. Feel free to tweak each of these messages here.
	|
	*/

	"accepted"             => "Mục :attribute phải được chọn đồng ý.",
	"active_url"           => "Mục :attribute không phải URL hợp lệ.",
	"after"                => "Mục :attribute phải sau ngày :date.",
	"alpha"                => "Mục :attribute chỉ được chứa chữ cái.",
	"alpha_dash"           => "Mục :attribute chỉ được chứa chữ cái, số và dấu gạch.",
	"alpha_num"            => "Mục :attribute chỉ được chứa chữ cái và chữ số.",
	"array"                => "Mục :attribute phải là một tập mảng.",
	"before"               => "Mục :attribute phải trước ngày :date.",
	"between"              => array(
		"numeric" => "Mục :attribute phải nằm giữa :min và :max.",
		"file"    => "Mục :attribute phải từ :min đến :max kilobytes.",
		"string"  => "Mục :attribute phải từ :min đến :max kí tự.",
		"array"   => "Mục :attribute phải chứa từ :min đến :max phần tử.",
	),
	"confirmed"            => "Mục xác nhận :attribute không khớp.",
	"date"                 => "Mục :attribute không phải là ngày hợp lệ.",
	"date_format"          => "Mục :attribute không đúng định dạng :format.",
	"different"            => "Mục :attribute và :other phải khác nhau.",
	"digits"               => "Mục :attribute phải có :digits chữ số.",
	"digits_between"       => "Mục :attribute phải từ :min đến :max chữ số.",
	"email"                => "Mục :attribute phải là địa chỉ email hợp lệ.",
	"exists"               => "Mục :attribute không có sẵn.",
	"image"                => "Mục :attribute phải là hình ảnh.",
	"in"                   => "Mục đã chọn :attribute không hợp lệ.",
	"integer"              => "Mục :attribute must be an integer.",
	"ip"                   => "Mục :attribute phải là địa chỉ IP hợp lệ.",
	"max"                  => array(
		"numeric" => "Mục :attribute không được vượt quá :max.",
		"file"    => "Mục :attribute không được vượt quá :max kilobytes.",
		"string"  => "Mục :attribute không được vượt quá :max kí tự.",
		"array"   => "Mục :attribute không được vượt quá :max phần tử.",
	),
	"mimes"                => "Mục :attribute phải là tập tin có định dạng: :values.",
	"min"                  => array(
		"numeric" => "Mục :attribute phải ít nhất là :min.",
		"file"    => "Mục :attribute phải ít nhất là :min kilobytes.",
		"string"  => "Mục :attribute phải có ít nhất :min kí tự.",
		"array"   => "Mục :attribute phải có ít nhất :min phần tử.",
	),
	"not_in"               => "Mục giá trị :attribute không hợp lệ.",
	"numeric"              => "Mục :attribute phải là số.",
	"regex"                => "Mục :attribute có định dạng không đúng.",
	"required"             => "Mục :attribute là bắt buộc.",
	"required_if"          => "Mục :attribute là bắt buộc khi :other bằng :value.",
	"required_with"        => "Mục :attribute là bắt buộc khi mục :values co sẵn.",
	"required_with_all"    => "Mục :attribute là bắt buộc khi các mục :values có sẵn.",
	"required_without"     => "Mục :attribute chỉ có khi không có mục :values.",
	"required_without_all" 
		=> "Mục :attribute chỉ có khi không có mục nào trong :values có sẵn.",
	"same"                 => "Mục :attribute và :other phải giống nhau.",
	"size"                 => array(
		"numeric" => "Mục :attribute phải bằng :size.",
		"file"    => "Mục :attribute phải có kích thước :size kilobytes.",
		"string"  => "Mục :attribute phải chứa :size kí tự.",
		"array"   => "Mục :attribute phải chứa :size phần tử.",
	),
	"unique"               => "Giá trị :attribute đã tồn tại.",
	"url"                  => "Mục :attribute không đúng định dạng.",

	/*
	|--------------------------------------------------------------------------
	| Custom Validation Language Lines
	|--------------------------------------------------------------------------
	|
	| Here you may specify custom validation messages for attributes using the
	| convention "attribute.rule" to name the lines. This makes it quick to
	| specify a specific custom language line for a given attribute rule.
	|
	*/

	'custom' => array(
		'attribute-name' => array(
			'rule-name' => 'custom-message',
		),
	),

	/*
	|--------------------------------------------------------------------------
	| Custom Validation Attributes
	|--------------------------------------------------------------------------
	|
	| The following language lines are used to swap attribute place-holders
	| with something more reader friendly such as E-Mail Address instead
	| of "email". This simply helps us make messages a little cleaner.
	|
	*/

	'attributes' => array(),

];
